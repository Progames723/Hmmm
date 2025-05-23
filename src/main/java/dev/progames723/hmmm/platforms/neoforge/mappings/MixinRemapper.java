package dev.progames723.hmmm.platforms.neoforge.mappings;

import dev.progames723.hmmm.platforms.neoforge.include.net.fabricmc.mappingio.tree.MappingTree;
import org.spongepowered.asm.mixin.extensibility.IRemapper;

public class MixinRemapper implements IRemapper {
	protected final MappingTree mappings;
	protected final int fromId;
	protected final int toId;
	
	public MixinRemapper(MappingTree mappings, int fromId, int toId) {
		this.mappings = mappings;
		this.fromId = fromId;
		this.toId = toId;
	}
	
	@Override
	public String mapMethodName(String owner, String name, String desc) {
		final MappingTree.MethodMapping method = mappings.getMethod(owner, name, desc, fromId);
		return method == null ? name : method.getName(toId);
	}
	
	@Override
	public String mapFieldName(String owner, String name, String desc) {
		final MappingTree.FieldMapping field = mappings.getField(owner, name, desc, fromId);
		return field == null ? name : field.getName(toId);
	}
	
	@Override
	public String map(String typeName) {
		return mappings.mapClassName(typeName, fromId, toId);
	}
	
	@Override
	public String unmap(String typeName) {
		return mappings.mapClassName(typeName, toId, fromId);
	}
	
	@Override
	public String mapDesc(String desc) {
		return mappings.mapDesc(desc, fromId, toId);
	}
	
	@Override
	public String unmapDesc(String desc) {
		return mappings.mapDesc(desc, toId, fromId);
	}
}
