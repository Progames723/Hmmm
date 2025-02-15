package dev.progames723.hmmm.internal;

import dev.progames723.hmmm.event.api.EventHandler;
import dev.progames723.hmmm.event.api.EventListener;
import dev.progames723.hmmm.event.events.LivingEntityEvent;
import dev.progames723.hmmm.utils.MiscUtil;
import dev.progames723.hmmm.utils.ReflectUtil;
import net.minecraft.world.entity.LivingEntity;
import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.classes.ClassCriteria;
import org.burningwave.core.classes.SearchConfig;
import org.burningwave.core.io.PathHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static dev.progames723.hmmm.utils.ReflectUtil.CALLER_CLASS;

/**
 * mainly for mixins and stuff
 */
public class EventUtils {
	private EventUtils() {MiscUtil.instantiationOfUtilClass(ReflectUtil.CALLER_CLASS.getCallerClass());}
	
	@CallerSensitive
	public static <T> LivingEntityEvent<T> createLivingEntityEvent(T value, LivingEntity entity) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed(CALLER_CLASS.getCallerClass(), List.of(LivingEntity.class), List.of(), List.of(), List.of());
		return new LivingEntityEvent<>(false, false, value, entity);
	}
	
	@CallerSensitive
	public static <T> LivingEntityEvent<T> createVoidLivingEntityEvent(T value, LivingEntity entity) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed(CALLER_CLASS.getCallerClass(), List.of(LivingEntity.class), List.of(), List.of(), List.of());
		return new LivingEntityEvent<>(false, true, value, entity);
	}
	
	@CallerSensitive
	public static LivingEntityEvent<Void> createVoidLivingEntityEvent(LivingEntity entity) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed(CALLER_CLASS.getCallerClass(), List.of(LivingEntity.class), List.of(), List.of(), List.of());
		return createVoidLivingEntityEvent(null, entity);
	}
	
	@CallerSensitive
	public static <T> LivingEntityEvent<T> createLivingEntityEventCancellable(T value, LivingEntity entity) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed(CALLER_CLASS.getCallerClass(), List.of(LivingEntity.class), List.of(), List.of(), List.of());
		return new LivingEntityEvent<>(true, false, value, entity);
	}
	
	@CallerSensitive
	public static <T> LivingEntityEvent<T> createVoidLivingEntityEventCancellable(T value, LivingEntity entity) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed(CALLER_CLASS.getCallerClass(), List.of(LivingEntity.class), List.of(), List.of(), List.of());
		return new LivingEntityEvent<>(true, true, value, entity);
	}
	
	@CallerSensitive
	public static <T> LivingEntityEvent<T> createVoidLivingEntityEventCancellable(LivingEntity entity) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed(CALLER_CLASS.getCallerClass(), List.of(LivingEntity.class), List.of(), List.of(), List.of());
		return createVoidLivingEntityEventCancellable(null, entity);
	}
	
	@CallerSensitive
	public static Collection<Class<?>> getClasses() {
		CallerSensitive.Utils.throwExceptionIfNotAllowed(CALLER_CLASS.getCallerClass(), List.of(), List.of(), List.of(), List.of());
		PathHelper helper = ComponentContainer.getInstance().getPathHelper();
		try (ClassCriteria criteria = ClassCriteria.create()) {
			return ComponentContainer.getInstance().getClassHunter().findBy(SearchConfig
					.byCriteria(criteria.allThoseThatMatch(clazz -> clazz.getDeclaredAnnotation(EventListener.class) != null))
					.addPaths(helper.getAllPaths())
				).getClasses();
		}
	}
	
	public static class EventInvocation {
		private EventInvocation() {MiscUtil.instantiationOfUtilClass(ReflectUtil.CALLER_CLASS.getCallerClass());}
		
		static final Collection<Method> eventMethods;
		
		static {
			Collection<Method> collection = new ArrayList<>();
			for (Class<?> clazz : getClasses()) {
				for (Method method : clazz.getDeclaredMethods()) if (method.getDeclaredAnnotation(EventHandler.class) != null) collection.add(method);
			}
			eventMethods = collection;
		}
		
		@CallerSensitive
		public void init() {
			CallerSensitive.Utils.throwExceptionIfNotAllowed(CALLER_CLASS.getCallerClass(), List.of(), List.of(), List.of(), List.of());
			//runs static initialization
		}
	}
}
