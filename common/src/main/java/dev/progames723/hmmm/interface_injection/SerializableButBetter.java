package dev.progames723.hmmm.interface_injection;

import dev.progames723.hmmm.ActualSecureRandom;

import java.io.Serial;
import java.io.Serializable;

public interface SerializableButBetter extends Serializable {
	@Serial
	long serialVersionUID = ActualSecureRandom.createSecureRandom().nextLong(Long.MIN_VALUE, Long.MAX_VALUE);
	
}
