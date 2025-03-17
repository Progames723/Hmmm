package dev.progames723.hmmm.forge;

import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.ReflectionMappingsImpl;
import dev.progames723.hmmm.forge.include.net.fabricmc.mappingio.MappingReader;
import dev.progames723.hmmm.forge.include.net.fabricmc.mappingio.adapter.FilteringMappingVisitor;
import dev.progames723.hmmm.forge.include.net.fabricmc.mappingio.format.MappingFormat;
import dev.progames723.hmmm.forge.include.net.fabricmc.mappingio.format.tiny.Tiny1FileReader;
import dev.progames723.hmmm.forge.include.net.fabricmc.mappingio.format.tiny.Tiny2FileReader;
import dev.progames723.hmmm.forge.include.net.fabricmc.mappingio.tree.MappingTree;
import dev.progames723.hmmm.forge.include.net.fabricmc.mappingio.tree.MemoryMappingTree;
import dev.progames723.hmmm.forge.mappings.MixinIntermediaryDevRemapper;
import org.jetbrains.annotations.Nullable;
import org.slf4j.MarkerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.zip.ZipError;

public class ReflectionMappingsNeoForgeImpl extends ReflectionMappingsImpl {
	private static final MemoryMappingTree mappings = (MemoryMappingTree) new MappingConfiguration().getMappings();
	
	private static final MixinIntermediaryDevRemapper mapper = new MixinIntermediaryDevRemapper(mappings, "named", "intermediary");
	
	private static final MixinIntermediaryDevRemapper unmapper = new MixinIntermediaryDevRemapper(mappings, "intermediary", "named");
	
	public ReflectionMappingsNeoForgeImpl() {super();}
	
	@Override
	public String mapClassName(String className) {
		return mappings.mapClassName(className, mappings.getNamespaceId("named"), mappings.getNamespaceId("intermediary"));
	}
	
	@Override
	public String mapField(String className, String field, String descriptor) {
		return mapper.mapFieldName(className, field, descriptor);
	}
	
	@Override
	public String mapMethod(String className, String method, String descriptor) {
		return mapper.mapMethodName(className, method, descriptor);
	}
	
	@Override
	public String unmapClassName(String className) {
		return mappings.mapClassName(className, mappings.getNamespaceId("intermediary"), mappings.getNamespaceId("named"));
	}
	
	@Override
	public String unmapField(String className, String field, String descriptor) {
		return unmapper.mapFieldName(className, field, descriptor);
	}
	
	@Override
	public String unmapMethod(String className, String method, String descriptor) {
		return unmapper.mapMethodName(className, method, descriptor);
	}
	
	private static class MappingConfiguration {
		private boolean initializedMappings = false;
		private boolean initializedMetadata = false;
		private List<String> namespaces;
		private @Nullable MemoryMappingTree mappings;
		
		private MappingConfiguration() {}
		
		@Nullable
		public List<String> getNamespaces() {
			initializeMetadata();
			return namespaces;
		}
		
		public MappingTree getMappings() {
			initializeMappings();
			return mappings;
		}
		
		private void initializeMetadata() {
			if (!this.initializedMetadata) {
				URLConnection connection = this.openMappings();
				try {
					if (connection != null) {
						BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						try {
							MappingFormat format = this.readMappingFormat(reader);
							switch (format) {
								case TINY_FILE:
									namespaces = Tiny1FileReader.getNamespaces(reader);
									break;
								case TINY_2_FILE:
									namespaces = Tiny2FileReader.getNamespaces(reader);
									break;
								default:
									throw new UnsupportedOperationException("Unsupported mapping format: " + format);
							}
						} catch (Throwable var6) {
							try {
								reader.close();
							} catch (Throwable var5) {
								var6.addSuppressed(var5);
							}
							throw var6;
						}
						reader.close();
					}
				} catch (IOException var7) {
					throw new RuntimeException("Error reading mapping metadata", var7);
				}
				this.initializedMetadata = true;
			}
		}
		
		private void initializeMappings() {
			if (!this.initializedMappings) {
				this.initializeMetadata();
				URLConnection connection = this.openMappings();
				if (connection != null) {
					try {
						BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						try {
							long time = System.currentTimeMillis();
							this.mappings = new MemoryMappingTree();
							FilteringMappingVisitor mappingFilter = new FilteringMappingVisitor(this.mappings);
							MappingFormat format = this.readMappingFormat(reader);
							switch (format) {
								case TINY_FILE:
									Tiny1FileReader.read(reader, mappingFilter);
									break;
								case TINY_2_FILE:
									Tiny2FileReader.read(reader, mappingFilter);
									break;
								default:
									throw new UnsupportedOperationException("Unsupported mapping format: " + format);
							}
//							HmmmLibrary.LOGGER.debug(MarkerFactory.getMarker("Mappings"), "Loading mappings took %d ms", new Object[]{System.currentTimeMillis() - time});
						} catch (Throwable var8) {
							try {
								reader.close();
							} catch (Throwable var7) {
								var8.addSuppressed(var7);
							}
							
							throw var8;
						}
						
						reader.close();
					} catch (IOException var9) {
						throw new RuntimeException("Error reading mappings", var9);
					}
				}
				
				if (this.mappings == null) {
					HmmmLibrary.LOGGER.info(MarkerFactory.getMarker("Mappings"), "Mappings not present!");
					this.mappings = new MemoryMappingTree();
				}
				
				this.initializedMappings = true;
			}
		}
		
		private @Nullable URLConnection openMappings() {
			URL url = MappingConfiguration.class.getClassLoader().getResource("mappings/mappings.tiny");
			if (url != null) {
				try {
					return url.openConnection();
				} catch (ZipError | IOException var3) {
					throw new RuntimeException("Error reading " + url, var3);
				}
			} else {
				return null;
			}
		}
		
		private MappingFormat readMappingFormat(BufferedReader reader) throws IOException {
			// We will only ever need to read tiny here
			// so to strip the other formats from the included copy of mapping IO, don't use MappingReader.read()
			reader.mark(4096);
			final MappingFormat format = MappingReader.detectFormat(reader);
			reader.reset();
			
			return format;
		}
	}
}
