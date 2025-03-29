package dev.progames723.hmmm.neoforge.mappings;

import dev.progames723.hmmm.neoforge.include.net.fabricmc.mappingio.tree.MappingTree;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

import java.util.*;

@SuppressWarnings("unused")
public class MixinIntermediaryDevRemapper extends MixinRemapper {
	private static final String ambiguousName = "<ambiguous>";
	private final Set<String> allPossibleClassNames = new HashSet<>();
	private final Map<String, String> nameMethodLookup = new HashMap<>();
	private final Map<String, String> nameDescFieldLookup = new HashMap<>();
	private final Map<String, String> nameDescMethodLookup = new HashMap<>();
	
	public MixinIntermediaryDevRemapper(MappingTree mappings, String from, String to) {
		super(mappings, mappings.getNamespaceId(from), mappings.getNamespaceId(to));
		
		for (MappingTree.ClassMapping classDef : mappings.getClasses()) {
			this.allPossibleClassNames.add(classDef.getName(from));
			this.allPossibleClassNames.add(classDef.getName(to));
			this.putMemberInLookup(this.fromId, this.toId, classDef.getFields(), new HashMap<>(), this.nameDescFieldLookup);
			this.putMemberInLookup(this.fromId, this.toId, classDef.getMethods(), this.nameMethodLookup, this.nameDescMethodLookup);
		}
		
	}
	
	private <T extends MappingTree.MemberMapping> void putMemberInLookup(int from, int to, Collection<T> descriptored, Map<String, String> nameMap, Map<String, String> nameDescMap) {
		
		for (T t : descriptored) {
			String nameFrom = t.getName(from);
			String descFrom = t.getDesc(from);
			String nameTo = t.getName(to);
			String prev = nameMap.putIfAbsent(nameFrom, nameTo);
			if (prev != null && !prev.equals("<ambiguous>") && !prev.equals(nameTo)) {
				nameDescMap.put(nameFrom, "<ambiguous>");
			}
			
			String key = getNameDescKey(nameFrom, descFrom);
			prev = nameDescMap.putIfAbsent(key, nameTo);
			if (prev != null && !prev.equals("<ambiguous>") && !prev.equals(nameTo)) {
				nameDescMap.put(key, "<ambiguous>");
			}
		}
		
	}
	
	private void throwAmbiguousLookup(String type, String name, String desc) {
		throw new RuntimeException("Ambiguous Mixin: " + type + " lookup " + name + " " + desc + " is not unique");
	}
	
	private String mapMethodNameInner(String owner, String name, String desc) {
		String result = super.mapMethodName(owner, name, desc);
		if (result.equals(name)) {
			String otherClass = this.unmap(owner);
			return super.mapMethodName(otherClass, name, this.unmapDesc(desc));
		} else {
			return result;
		}
	}
	
	private String mapFieldNameInner(String owner, String name, String desc) {
		String result = super.mapFieldName(owner, name, desc);
		if (result.equals(name)) {
			String otherClass = this.unmap(owner);
			return super.mapFieldName(otherClass, name, this.unmapDesc(desc));
		} else {
			return result;
		}
	}
	
	public String mapMethodName(String owner, String name, String desc) {
		String unmapDesc;
		if (owner == null || this.allPossibleClassNames.contains(owner)) {
			String newName;
			if (desc == null) {
				newName = this.nameMethodLookup.get(name);
			} else {
				newName = this.nameDescMethodLookup.get(getNameDescKey(name, desc));
			}
			
			if (newName == null) {
				if (owner == null) {
					return name;
				}
				
				String unmapOwner = this.unmap(owner);
				unmapDesc = this.unmapDesc(desc);
				if (unmapOwner.equals(owner) && unmapDesc.equals(desc)) {
					return name;
				}
				
				return this.mapMethodName(unmapOwner, name, unmapDesc);
			}
			
			if (!newName.equals("<ambiguous>")) {
				return newName;
			}
			
			if (owner == null) {
				this.throwAmbiguousLookup("method", name, desc);
			}
		}
		
		ClassInfo classInfo = ClassInfo.forName(this.map(owner));
		if (classInfo != null) {
			Queue<ClassInfo> queue = new ArrayDeque<>();
			
			do {
				unmapDesc = this.unmap(classInfo.getName());
				String s;
				if (!(s = this.mapMethodNameInner(unmapDesc, name, desc)).equals(name)) {
					return s;
				}
				
				if (classInfo.getSuperName() != null && !classInfo.getSuperName().startsWith("java/")) {
					ClassInfo cSuper = classInfo.getSuperClass();
					if (cSuper != null) {
						queue.add(cSuper);
					}
				}
				
				for (String itf : classInfo.getInterfaces()) {
					if (!itf.startsWith("java/")) {
						ClassInfo cItf = ClassInfo.forName(itf);
						if (cItf != null) {
							queue.add(cItf);
						}
					}
				}
			} while ((classInfo = queue.poll()) != null);
			
		}
		return name;
	}
	
	public String mapFieldName(String owner, String name, String desc) {
		String unmapOwner;
		String unmapDesc;
		if (owner == null || this.allPossibleClassNames.contains(owner)) {
			String newName = this.nameDescFieldLookup.get(getNameDescKey(name, desc));
			if (newName == null) {
				if (owner == null) {
					return name;
				}
				
				unmapOwner = this.unmap(owner);
				unmapDesc = this.unmapDesc(desc);
				if (unmapOwner.equals(owner) && unmapDesc.equals(desc)) {
					return name;
				}
				
				return this.mapFieldName(unmapOwner, name, unmapDesc);
			}
			
			if (!newName.equals("<ambiguous>")) {
				return newName;
			}
			
			if (owner == null) {
				this.throwAmbiguousLookup("field", name, desc);
			}
		}
		
		for(ClassInfo c = ClassInfo.forName(this.map(owner)); c != null; c = c.getSuperClass()) {
			unmapOwner = this.unmap(c.getName());
			unmapDesc = this.mapFieldNameInner(unmapOwner, name, desc);
			if (!unmapDesc.equals(name)) {
				return unmapDesc;
			}
			
			if (c.getSuperName() == null || c.getSuperName().startsWith("java/")) {
				break;
			}
		}
		
		return name;
	}
	
	private static String getNameDescKey(String name, String descriptor) {
		return name + ";;" + descriptor;
	}
}
