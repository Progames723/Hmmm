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

package dev.progames723.hmmm.platforms.neoforge.include.net.fabricmc.mappingio.tree;

import dev.progames723.hmmm.platforms.neoforge.include.net.fabricmc.mappingio.MappingVisitor;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Read-only mapping tree.
 */
public interface MappingTreeView {
	/**
	 * @return The source namespace, or {@code null} if the tree is uninitialized.
	 */
	@Nullable
	String getSrcNamespace();

	/**
	 * @return A list containing the destination namespaces, in order of their IDs.
	 * Can only be empty if the tree is uninitialized.
	 */
	List<String> getDstNamespaces();
	
	default int getNamespaceId(String namespace) {
		if (namespace.equals(getSrcNamespace())) {
			return SRC_NAMESPACE_ID;
		}

		int ret = getDstNamespaces().indexOf(namespace);

		return ret >= 0 ? ret : NULL_NAMESPACE_ID;
	}
	
	Collection<? extends ClassMappingView> getClasses();
	@Nullable
	ClassMappingView getClass(String srcName);
	@Nullable
	default ClassMappingView getClass(String name, int namespace) {
		if (namespace < 0) return getClass(name);

		for (ClassMappingView cls : getClasses()) {
			if (name.equals(cls.getDstName(namespace))) return cls;
		}

		return null;
	}

	/**
	 * @see MappingTreeView#getField(String, String, String, int)
	 */
	@Nullable
	default FieldMappingView getField(String srcClsName, String srcName, @Nullable String srcDesc) {
		ClassMappingView owner = getClass(srcClsName);
		return owner != null ? owner.getField(srcName, srcDesc) : null;
	}

	@Nullable
	default FieldMappingView getField(String clsName, String name, @Nullable String desc, int namespace) {
		ClassMappingView owner = getClass(clsName, namespace);
		return owner != null ? owner.getField(name, desc, namespace) : null;
	}

	/**
	 * @see MappingTreeView#getMethod(String, String, String, int)
	 */
	@Nullable
	default MethodMappingView getMethod(String srcClsName, String srcName, @Nullable String srcDesc) {
		ClassMappingView owner = getClass(srcClsName);
		return owner != null ? owner.getMethod(srcName, srcDesc) : null;
	}

	/**
	 * @param desc Can be either complete desc or parameter-only desc.
	 */
	@Nullable
	default MethodMappingView getMethod(String clsName, String name, @Nullable String desc, int namespace) {
		ClassMappingView owner = getClass(clsName, namespace);
		return owner != null ? owner.getMethod(name, desc, namespace) : null;
	}

	default void accept(MappingVisitor visitor) throws IOException {
		accept(visitor, VisitOrder.createByInputOrder());
	}

	void accept(MappingVisitor visitor, VisitOrder order) throws IOException;
	
	default String mapClassName(String name, int srcNamespace, int dstNamespace) {
		assert name.indexOf('.') < 0;

		if (srcNamespace == dstNamespace) return name;

		ClassMappingView cls = getClass(name, srcNamespace);
		if (cls == null) return name;

		String ret = cls.getName(dstNamespace);

		return ret != null ? ret : name;
	}

	default String mapDesc(CharSequence desc, int namespace) {
		return mapDesc(desc, 0, desc.length(), SRC_NAMESPACE_ID, namespace);
	}

	default String mapDesc(CharSequence desc, int srcNamespace, int dstNamespace) {
		return mapDesc(desc, 0, desc.length(), srcNamespace, dstNamespace);
	}
	
	default String mapDesc(CharSequence desc, int start, int end, int srcNamespace, int dstNamespace) {
		if (srcNamespace == dstNamespace) return desc.subSequence(start, end).toString();

		StringBuilder ret = null;
		int copyOffset = start;
		int offset = start;

		while (offset < end) {
			char c = desc.charAt(offset++);

			if (c == 'L') {
				int idEnd = offset; // current identifier end, exclusive

				while (idEnd < end) {
					c = desc.charAt(idEnd);
					if (c == ';') break;
					idEnd++;
				}

				if (idEnd >= end) throw new IllegalArgumentException("invalid descriptor: "+desc.subSequence(start, end));

				String cls = desc.subSequence(offset, idEnd).toString();
				String mappedCls = mapClassName(cls, srcNamespace, dstNamespace);

				if (mappedCls != null && !mappedCls.equals(cls)) {
					if (ret == null) ret = new StringBuilder(end - start);

					ret.append(desc, copyOffset, offset);
					ret.append(mappedCls);
					copyOffset = idEnd;
				}

				offset = idEnd + 1;
			}
		}

		if (ret == null) return desc.subSequence(start, end).toString();

		ret.append(desc, copyOffset, end);

		return ret.toString();
	}

	interface MetadataEntryView {
		String getKey();
		@Nullable
		String getValue();
	}

	interface ElementMappingView {
		MappingTreeView getTree();

		String getSrcName();
		@Nullable
		String getDstName(int namespace);

		@Nullable
		default String getName(int namespace) {
			if (namespace < 0) {
				return getSrcName();
			} else {
				return getDstName(namespace);
			}
		}

		@Nullable
		default String getName(String namespace) {
			int nsId = getTree().getNamespaceId(namespace);

			if (nsId == NULL_NAMESPACE_ID) {
				return null;
			} else {
				return getName(nsId);
			}
		}

		@Nullable
		String getComment();
	}

	interface ClassMappingView extends ElementMappingView {
		Collection<? extends FieldMappingView> getFields();

		/**
		 * @see MappingTreeView#getField(String, String, String, int)
		 */
		@Nullable
		FieldMappingView getField(String srcName, @Nullable String srcDesc);

		/**
		 * @see MappingTreeView#getField(String, String, String, int)
		 */
		@Nullable
		default FieldMappingView getField(String name, @Nullable String desc, int namespace) {
			if (namespace < 0) return getField(name, desc);

			for (FieldMappingView field : getFields()) {
				if (!name.equals(field.getDstName(namespace))) continue;
				String mDesc;
				if (desc != null && (mDesc = field.getDesc(namespace)) != null && !desc.equals(mDesc)) continue;

				return field;
			}

			return null;
		}

		Collection<? extends MethodMappingView> getMethods();

		/**
		 * @see MappingTreeView#getMethod(String, String, String, int)
		 */
		@Nullable
		MethodMappingView getMethod(String srcName, @Nullable String srcDesc);

		/**
		 * @see MappingTreeView#getMethod(String, String, String, int)
		 */
		@Nullable
		default MethodMappingView getMethod(String name, @Nullable String desc, int namespace) {
			if (namespace < 0) return getMethod(name, desc);

			for (MethodMappingView method : getMethods()) {
				if (!name.equals(method.getDstName(namespace))) continue;

				String mDesc;
				if (desc != null && (mDesc = method.getDesc(namespace)) != null && !desc.equals(mDesc) && !(desc.endsWith(")") && mDesc.startsWith(desc))) continue;

				return method;
			}

			return null;
		}
	}

	interface MemberMappingView extends ElementMappingView {
		ClassMappingView getOwner();
		@Nullable
		String getSrcDesc();
		
		@Nullable
		default String getDesc(int namespace) {
			String srcDesc = getSrcDesc();

			if (namespace < 0 || srcDesc == null) {
				return srcDesc;
			} else {
				return getTree().mapDesc(srcDesc, namespace);
			}
		}
		
	}

	interface FieldMappingView extends MemberMappingView { }

	interface MethodMappingView extends MemberMappingView {
		Collection<? extends MethodArgMappingView> getArgs();
		@Nullable
		MethodArgMappingView getArg(int argPosition, int lvIndex, @Nullable String srcName);

		Collection<? extends MethodVarMappingView> getVars();
		@Nullable
		MethodVarMappingView getVar(int lvtRowIndex, int lvIndex, int startOpIdx, int endOpIdx, @Nullable String srcName);
	}

	interface MethodArgMappingView extends ElementMappingView {
		MethodMappingView getMethod();
		int getArgPosition();
		int getLvIndex();
	}

	interface MethodVarMappingView extends ElementMappingView {
		MethodMappingView getMethod();
		int getLvtRowIndex();
		int getLvIndex();
		int getStartOpIdx();
		int getEndOpIdx();
	}

	int SRC_NAMESPACE_ID = -1;
	int NULL_NAMESPACE_ID = -2;
}
