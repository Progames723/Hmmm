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

package dev.progames723.hmmm.platforms.neoforge.include.net.fabricmc.mappingio.format.tiny;

import dev.progames723.hmmm.platforms.neoforge.include.net.fabricmc.mappingio.MappedElementKind;
import dev.progames723.hmmm.platforms.neoforge.include.net.fabricmc.mappingio.MappingFlag;
import dev.progames723.hmmm.platforms.neoforge.include.net.fabricmc.mappingio.MappingVisitor;
import dev.progames723.hmmm.platforms.neoforge.include.net.fabricmc.mappingio.format.ColumnFileReader;
import dev.progames723.hmmm.platforms.neoforge.include.net.fabricmc.mappingio.format.MappingFormat;
import dev.progames723.hmmm.platforms.neoforge.include.net.fabricmc.mappingio.tree.MappingTree;
import dev.progames723.hmmm.platforms.neoforge.include.net.fabricmc.mappingio.tree.MemoryMappingTree;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * {@linkplain MappingFormat#TINY_FILE Tiny v1 file} reader.
 *
 * <p>Crashes if a second visit pass is requested without
 * {@link MappingFlag#NEEDS_MULTIPLE_PASSES} having been passed beforehand.
 */
public final class Tiny1FileReader {
	private Tiny1FileReader() {
	}

	public static List<String> getNamespaces(Reader reader) throws IOException {
		return getNamespaces(new ColumnFileReader(reader, '\t', '\t'));
	}

	private static List<String> getNamespaces(ColumnFileReader reader) throws IOException {
		if (!reader.nextCol("v1")) { // magic/version
			throw new IOException("invalid/unsupported tiny file: no tiny 1 header");
		}

		List<String> ret = new ArrayList<>();
		String ns;

		while ((ns = reader.nextCol()) != null) {
			ret.add(ns);
		}

		return ret;
	}

	public static void read(Reader reader, MappingVisitor visitor) throws IOException {
		read(new ColumnFileReader(reader, '\t', '\t'), visitor);
	}

	private static void read(ColumnFileReader reader, MappingVisitor visitor) throws IOException {
		if (!reader.nextCol("v1")) { // magic/version
			throw new IOException("invalid/unsupported tiny file: no tiny 1 header");
		}

		String srcNamespace = reader.nextCol();
		List<String> dstNamespaces = new ArrayList<>();
		String dstNamespace;

		while ((dstNamespace = reader.nextCol()) != null) {
			dstNamespaces.add(dstNamespace);
		}

		int dstNsCount = dstNamespaces.size();
		Set<MappingFlag> flags = visitor.getFlags();
		MappingVisitor parentVisitor = null;
		boolean readerMarked = false;

		if (flags.contains(MappingFlag.NEEDS_ELEMENT_UNIQUENESS) || flags.contains(MappingFlag.NEEDS_HEADER_METADATA)) {
			parentVisitor = visitor;
			visitor = new MemoryMappingTree();
		} else if (flags.contains(MappingFlag.NEEDS_MULTIPLE_PASSES)) {
			reader.mark();
			readerMarked = true;
		}

		for (;;) {
			if (visitor.visitHeader()) {
				visitor.visitNamespaces(srcNamespace, dstNamespaces);
			}

			if (visitor.visitContent()) {
				String lastClass = null;
				boolean lastClassDstNamed = false;;
				boolean visitLastClass = false;

				while (reader.nextLine(0)) {
					boolean isMethod;

					if (reader.nextCol("CLASS")) { // class: CLASS <names>...
						String srcName = reader.nextCol();
						if (srcName == null || srcName.isEmpty()) throw new IOException("missing class-name-a in line "+reader.getLineNumber());

						if (!lastClassDstNamed || !srcName.equals(lastClass)) {
							lastClass = srcName;
							lastClassDstNamed = true;
							visitLastClass = visitor.visitClass(srcName);

							if (visitLastClass) {
								readDstNames(reader, MappedElementKind.CLASS, dstNsCount, visitor);
								visitLastClass = visitor.visitElementContent(MappedElementKind.CLASS);
							}
						}
					} else if ((isMethod = reader.nextCol("METHOD")) || reader.nextCol("FIELD")) { // method: METHOD cls-a desc-a <names>... or field: FIELD cls-a desc-a <names>...
						String srcOwner = reader.nextCol();
						if (srcOwner == null || srcOwner.isEmpty()) throw new IOException("missing class-name-a in line "+reader.getLineNumber());

						if (!srcOwner.equals(lastClass)) {
							lastClass = srcOwner;
							lastClassDstNamed = false;
							visitLastClass = visitor.visitClass(srcOwner) && visitor.visitElementContent(MappedElementKind.CLASS);
						}

						if (visitLastClass) {
							String srcDesc = reader.nextCol();
							if (srcDesc == null || srcDesc.isEmpty()) throw new IOException("missing desc-a in line "+reader.getLineNumber());
							String srcName = reader.nextCol();
							if (srcName == null || srcName.isEmpty()) throw new IOException("missing name-a in line "+reader.getLineNumber());

							if (isMethod && visitor.visitMethod(srcName, srcDesc)
									|| !isMethod && visitor.visitField(srcName, srcDesc)) {
								MappedElementKind kind = isMethod ? MappedElementKind.METHOD : MappedElementKind.FIELD;
								readDstNames(reader, kind, dstNsCount, visitor);
								visitor.visitElementContent(kind);
							}
						}
					} else {
						String line = reader.nextCol();
						final String prefix = "# INTERMEDIARY-COUNTER ";
						String[] parts;

						if (line.startsWith(prefix)
								&& (parts = line.substring(prefix.length()).split(" ")).length == 2) {
							String property = null;

							switch (parts[0]) {
							case "class":
								property = nextIntermediaryClassProperty;
								break;
							case "field":
								property = nextIntermediaryFieldProperty;
								break;
							case "method":
								property = nextIntermediaryMethodProperty;
								break;
							}

							if (property != null) {
								visitor.visitMetadata(property, parts[1]);
							}
						}
					}
				}
			}

			if (visitor.visitEnd()) break;

			if (!readerMarked) {
				throw new IllegalStateException("repeated visitation requested without NEEDS_MULTIPLE_PASSES");
			}

			int markIdx = reader.reset();
			assert markIdx == 1;
		}

		if (parentVisitor != null) {
			((MappingTree) visitor).accept(parentVisitor);
		}
	}

	private static void readDstNames(ColumnFileReader reader, MappedElementKind subjectKind, int dstNsCount, MappingVisitor visitor) throws IOException {
		for (int dstNs = 0; dstNs < dstNsCount; dstNs++) {
			String name = reader.nextCol();
			if (name == null) throw new IOException("missing name columns in line "+reader.getLineNumber());

			if (!name.isEmpty()) visitor.visitDstName(subjectKind, dstNs, name);
		}
	}

	static final String nextIntermediaryClassProperty = "next-intermediary-class";
	static final String nextIntermediaryFieldProperty = "next-intermediary-field";
	static final String nextIntermediaryMethodProperty = "next-intermediary-method";
}
