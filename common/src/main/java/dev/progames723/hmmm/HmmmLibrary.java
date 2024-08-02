package dev.progames723.hmmm;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.architectury.event.EventHandler;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.progames723.hmmm.utils.MathUtil;
import dev.progames723.hmmm.utils.PlatformUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.burningwave.core.assembler.ComponentSupplier;
import org.burningwave.core.classes.BodySourceGenerator;
import org.burningwave.core.classes.ExecuteConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class HmmmLibrary {
    public static final String MOD_ID = "hmmm";
    public static final Logger LOGGER = LoggerFactory.getLogger("HmmmLibrary");
    public static final Marker NATIVE = MarkerFactory.getMarker("Native");
    public static final Marker JAVA_COMMAND = MarkerFactory.getMarker("Java Command");
    
    public static void init() {
        LOGGER.info("Initializing HmmmLibrary");
        EventHandler.init();
        MathUtil.loadLibrary();
        LOGGER.info("Running system architecture: {}", PlatformUtil.getArchitecture());
        Events.loadAll();//does nothing for now
        LOGGER.info("Initialized HmmmLibrary!");
    }
    
    private static class Events {
        private static final CommandRegistrationEvent commandRegistrationEvent = (dispatcher, registry, selection) -> {
            AtomicReference<String> codeToExecute = new AtomicReference<>("");
            AtomicReference<CommandSourceStack> runner = new AtomicReference<>();
            dispatcher.register(LiteralArgumentBuilder.literal("execute_java"))
                    .createBuilder()
                    .then(LiteralArgumentBuilder.literal("code"))
                    .executes(context -> {
                        runner.set(context.getSource());
                        codeToExecute.set(context.getArgument("code", String.class));
                        final String stringCode = codeToExecute.get();
                        String[] multiLineCode = {};
                        BodySourceGenerator generator = BodySourceGenerator.create();
                        if (stringCode.contains("\\n")) {
                            stringCode.replace("\\n", "\n");
                            multiLineCode = stringCode.split("\\n");
                            for (String s : multiLineCode) {
                                generator.addCodeLine(s);
                            }
                        } else {
                            generator.addCodeLine(stringCode);
                        }
                        try {
                            ComponentSupplier.getInstance().getCodeExecutor().execute(ExecuteConfig.forBodySourceGenerator(
                                    generator
                            ));
                            context.getSource().sendSuccess(() -> Component.literal("Success!"), true);
                        } catch (Exception e) {
                            context.getSource().sendFailure(Component.literal("Unable to run provided java code. See console for more info"));
                            LOGGER.error(JAVA_COMMAND, "Unable to run provided java code", e);
                        }
                        return 0;
                    })
                    .build()
                    .listSuggestions(
                            new CommandContextBuilder<>(
                                    dispatcher,
                                    runner.get(),
                                    dispatcher.getRoot(),
                                    selection.ordinal()
                            ).build("java code"),
                            new SuggestionsBuilder(
                                    codeToExecute.get(),
                                    codeToExecute.get().toLowerCase(Locale.ROOT),
                                    selection.ordinal())
                    );
        };
        
        public static void loadAll() {
//          CommandRegistrationEvent.EVENT.register(commandRegistrationEvent);
        }
    }
}
