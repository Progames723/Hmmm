/*
 * Copyright (c) 2021 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.progames723.hmmm.include.net.fabricmc.mappingio.format.enigma;

import dev.progames723.hmmm.include.net.fabricmc.mappingio.MappingFlag;
import dev.progames723.hmmm.include.net.fabricmc.mappingio.MappingUtil;
import dev.progames723.hmmm.include.net.fabricmc.mappingio.MappingVisitor;
import dev.progames723.hmmm.include.net.fabricmc.mappingio.adapter.ForwardingMappingVisitor;
import dev.progames723.hmmm.include.net.fabricmc.mappingio.format.MappingFormat;
import dev.progames723.hmmm.include.net.fabricmc.mappingio.tree.MappingTree;
import dev.progames723.hmmm.include.net.fabricmc.mappingio.tree.MemoryMappingTree;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.Set;

/**
 * {@linkplain MappingFormat#ENIGMA_DIR Enigma directory} reader.
 *
 * <p>Crashes if a second visit pass is requested without
 * {@link MappingFlag#NEEDS_MULTIPLE_PASSES} having been passed beforehand.
 */
public final class EnigmaDirReader {
	private EnigmaDirReader() {
	}

	public static void read(Path dir, MappingVisitor visitor) throws IOException {
		read(dir, MappingUtil.NS_SOURCE_FALLBACK, MappingUtil.NS_TARGET_FALLBACK, visitor);
	}

	public static void read(Path dir, String sourceNs, String targetNs, MappingVisitor visitor) throws IOException {
		Set<MappingFlag> flags = visitor.getFlags();
		MappingVisitor parentVisitor = null;

		if (flags.contains(MappingFlag.NEEDS_ELEMENT_UNIQUENESS) || flags.contains(MappingFlag.NEEDS_MULTIPLE_PASSES)) {
			parentVisitor = visitor;
			visitor = new MemoryMappingTree();
		}

		if (visitor.visitHeader()) {
			visitor.visitNamespaces(sourceNs, Collections.singletonList(targetNs));
		}

		MappingVisitor delegatingVisitor = new ForwardingMappingVisitor(visitor) {
			@Override
			public boolean visitHeader() throws IOException {
				return false; // Namespaces have already been visited above, and Enigma files don't have any metadata
			}

			@Override
			public boolean visitContent() throws IOException {
				if (!visitedContent) { // Don't call next's visitContent() more than once
					visitedContent = true;
					visitContent = super.visitContent();
				}

				return visitContent;
			}

			@Override
			public boolean visitEnd() throws IOException {
				return true; // Don't forward since we're not done yet, there are more files to come
			}

			private boolean visitedContent;
			private boolean visitContent;
		};

		Files.walkFileTree(dir, new SimpleFileVisitor<>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (file.getFileName().toString().endsWith("." + MappingFormat.ENIGMA_FILE.fileExt)) {
					EnigmaFileReader.read(Files.newBufferedReader(file), sourceNs, targetNs, delegatingVisitor);
				}
				
				return FileVisitResult.CONTINUE;
			}
		});

		if (visitor.visitEnd() && parentVisitor == null) return;

		if (parentVisitor == null) {
			throw new IllegalStateException("repeated visitation requested without NEEDS_MULTIPLE_PASSES");
		}

		((MappingTree) visitor).accept(parentVisitor);
	}
}
