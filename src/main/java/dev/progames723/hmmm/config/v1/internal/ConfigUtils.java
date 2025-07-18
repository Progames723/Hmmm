package dev.progames723.hmmm.config.v1.internal;

import dev.progames723.hmmm.config.v1.Config;
import dev.progames723.hmmm.internal.CallerSensitive;
import dev.progames723.hmmm.utils.MiscUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public final class ConfigUtils {
	private ConfigUtils() {MiscUtil.instantiationOfUtilClass();}
	
	private static final Set<Config<?, ?>> configs = new HashSet<>();
	
	@CallerSensitive
	@ApiStatus.Internal
	public static Set<Config<?, ?>> getConfigsInternal() {
		CallerSensitive.Utils.throwExceptionIfNotAllowed();
		return configs;
	}
	
	@CallerSensitive
	@ApiStatus.Internal
	public static void registerConfig(Config<?, ?> config) {
		CallerSensitive.Utils.throwExceptionIfNotAllowed();
		configs.add(config);
	}
}
