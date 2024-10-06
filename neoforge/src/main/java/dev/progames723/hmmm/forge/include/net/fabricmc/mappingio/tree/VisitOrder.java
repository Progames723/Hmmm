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

package dev.progames723.hmmm.forge.include.net.fabricmc.mappingio.tree;

import dev.progames723.hmmm.forge.include.net.fabricmc.mappingio.MappingVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Visitation order configuration for {@link MappingTreeView#accept(MappingVisitor, VisitOrder)}.
 */
public final class VisitOrder {
	private VisitOrder() {
	}

	// pre-defined orders

	public static VisitOrder createByInputOrder() {
		return new VisitOrder();
	}
	
	// visit order configuration
	
	// customization helpers
	
	// application

	public <T extends MappingTreeView.ClassMappingView> Collection<T> sortClasses(Collection<T> classes) {
		return sort(classes, classComparator);
	}

	public <T extends MappingTreeView.FieldMappingView> Collection<T> sortFields(Collection<T> fields) {
		return sort(fields, fieldComparator);
	}

	public <T extends MappingTreeView.MethodMappingView> Collection<T> sortMethods(Collection<T> methods) {
		return sort(methods, methodComparator);
	}

	public <T extends MappingTreeView.MethodArgMappingView> Collection<T> sortMethodArgs(Collection<T> args) {
		return sort(args, methodArgComparator);
	}

	public <T extends MappingTreeView.MethodVarMappingView> Collection<T> sortMethodVars(Collection<T> vars) {
		return sort(vars, methodVarComparator);
	}

	private static <T> Collection<T> sort(Collection<T> inputs, Comparator<? super T> comparator) {
		if (comparator == null || inputs.size() < 2) return inputs;

		List<T> ret = new ArrayList<>(inputs);
		ret.sort(comparator);

		return ret;
	}

	public boolean isMethodsFirst() {
		return methodsFirst;
	}

	public boolean isMethodVarsFirst() {
		return methodVarsFirst;
	}

	private Comparator<MappingTreeView.ClassMappingView> classComparator;
	private Comparator<MappingTreeView.FieldMappingView> fieldComparator;
	private Comparator<MappingTreeView.MethodMappingView> methodComparator;
	private Comparator<MappingTreeView.MethodArgMappingView> methodArgComparator;
	private Comparator<MappingTreeView.MethodVarMappingView> methodVarComparator;
	private boolean methodsFirst;
	private boolean methodVarsFirst;
}
