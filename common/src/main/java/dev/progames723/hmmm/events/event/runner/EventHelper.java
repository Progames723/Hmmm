package dev.progames723.hmmm.events.event.runner;

import dev.architectury.platform.Platform;
import dev.progames723.hmmm.events.annotations.EventListener;
import dev.progames723.hmmm.events.annotations.EventListenerClass;
import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.assembler.ComponentSupplier;
import org.burningwave.core.classes.ClassCriteria;
import org.burningwave.core.classes.ClassHunter;
import org.burningwave.core.classes.MethodCriteria;
import org.burningwave.core.classes.SearchConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EventHelper {
	private static final List<Class<?>> manuallyRegisteredClasses = new ArrayList<>();
	
	private static Events events;
	
	static Collection<Method> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
		Collection<Method> collection = new ArrayList<>();
		
		Method[] methods = clazz.getDeclaredMethods();
		
		for (Method method : methods) {
			if (method.isAnnotationPresent(annotation)) collection.add(method);
		}
		
		return collection;
	}
	
	static Collection<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotation) {
		ComponentSupplier componentSupplier = ComponentContainer.getInstance();
		SearchConfig searchConfig = SearchConfig.forPaths(
			componentSupplier.getPathHelper().getMainClassPaths(),
			List.of(Platform.getModsFolder().toAbsolutePath().toFile().getAbsolutePath())
		).by(
			ClassCriteria.create().allThoseThatMatch(cls -> cls.isAnnotationPresent(annotation)).or()
				.byMembers(MethodCriteria.withoutConsideringParentClasses().allThoseThatMatch(method -> method.isAnnotationPresent(annotation))
			)
		);
		try (ClassHunter.SearchResult searchResult = componentSupplier.getClassHunter().findBy(searchConfig)) {
			return searchResult.getClasses();
		}
	}
	
	public static void manuallyAddListenerClass(Class<?> clazz) {
		if (clazz.isAnnotationPresent(EventListenerClass.class)) {
			manuallyRegisteredClasses.add(clazz);
		}
	}
	
	public static void initEvents() {
		Collection<Class<?>> annotatedClasses = getAnnotatedClasses(EventListenerClass.class);
		for (Class<?> clazz : manuallyRegisteredClasses) {
			if (!annotatedClasses.contains(clazz)) annotatedClasses.add(clazz);
		}
		
		Collection<Method> annotatedMethods = new ArrayList<>();
		
		for (Class<?> clazz : annotatedClasses) {
			annotatedMethods.addAll(getAnnotatedMethods(clazz, EventListener.class));
		}
		
		annotatedMethods = sortMethodsByPriority(annotatedMethods);
		
		events = new Events(annotatedClasses, annotatedMethods);
	}
	
	private static Collection<Method> sortMethodsByPriority(Collection<Method> methods) {
		ArrayList<Method> sortedList = new ArrayList<>();
		ArrayList<Method> h1List = new ArrayList<>();
		ArrayList<Method> hList = new ArrayList<>();
		ArrayList<Method> nList = new ArrayList<>();
		ArrayList<Method> lList = new ArrayList<>();
		ArrayList<Method> l1List = new ArrayList<>();
		for (Method method : methods) {
			switch (method.getAnnotation(EventListener.class).priority()) {
				case HIGHEST -> h1List.add(method);
				case HIGH -> hList.add(method);
				case NORMAL -> nList.add(method);
				case LOW -> lList.add(method);
				case LOWEST -> l1List.add(method);
				default -> throw new RuntimeException("Invalid enum");
			}
		}
		if (!sortedList.isEmpty()) //sanity checks
			throw new RuntimeException("List not empty when it should be!");
		sortedList.addAll(h1List);
		sortedList.addAll(hList);
		sortedList.addAll(nList);
		sortedList.addAll(lList);
		sortedList.addAll(l1List);
		//clean up
		h1List.clear();
		hList.clear();
		nList.clear();
		lList.clear();
		l1List.clear();
		return sortedList;
	}
}
