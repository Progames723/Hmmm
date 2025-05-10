package dev.progames723.hmmm.obj_attach;

import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.interface_injection.PlayerAttachments;
import dev.progames723.hmmm.utils.InternalUtils;
import dev.progames723.hmmm.utils.SerializationUtil;
import net.minecraft.Util;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttachmentProvider {
	static void saveAllAttachments(Player player, File playerDataDir, @Nullable UUID transferFrom) {
		for (AttachmentProvider provider : ((PlayerAttachments) player).getPlayerAttachments()) {
			if (provider == null) continue;
			try {
				saveAttachment(player, playerDataDir, provider, transferFrom);
			} catch (IOException e) {
				HmmmLibrary.LOGGER.info("IOException while saving attachments!");
			}
		}
	}
	
	static <T extends AttachmentProvider> void saveAttachment(Player player, File playerDataDir, T attachment, @Nullable UUID transferFrom) throws IOException {
		playerDataDir.mkdirs();
		if (attachment == null) return;
		
		File tempFile = File.createTempFile(transferFrom != null ? transferFrom.toString() : player.getStringUUID() + "-", ".dat", playerDataDir);
		try (InputStream stream = new ByteArrayInputStream(SerializationUtil.serializeAndCompressObject(attachment))) {
			try (FileOutputStream fos = new FileOutputStream(tempFile)) {
				stream.transferTo(fos);
			}
		}
		
		File file2 = new File(playerDataDir, transferFrom != null ? transferFrom.toString() : player.getStringUUID() + "-" + attachment.getClass().getSimpleName() + ".dat");
		File file3 = new File(playerDataDir, transferFrom != null ? transferFrom.toString() : player.getStringUUID() + "-" + attachment.getClass().getSimpleName() + ".dat_old");
		file2.createNewFile();
		file3.createNewFile();
		Util.safeReplaceFile(file2.toPath(), tempFile.toPath(), file3.toPath());
	}
	
	static void tryLoadAttachmentsForPlayer(Player player, File playerDataDir, @Nullable UUID transferFrom) {
		List<Class<AttachmentProvider>> classes = InternalUtils.scanClassesForGenerics(AttachmentProvider.class, InternalUtils.ScanType.INTERFACE_IMPL, true);
		if (classes.isEmpty()) HmmmLibrary.LOGGER.info("No attachments found for player {}", player);
		else classes.forEach(attachmentProviderClass -> {
			try {
				Optional<AttachmentProvider> attachmentProvider = findAttachment(player, playerDataDir, attachmentProviderClass, transferFrom);
				if (attachmentProvider.isEmpty()) return;
				if (((PlayerAttachments) player).getPlayerAttachment(attachmentProviderClass).isEmpty())
					((PlayerAttachments) player).addPlayerAttachment(attachmentProvider.get());
			} catch (IOException e) {
				HmmmLibrary.LOGGER.info("IOException while trying to load attachments!", e);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	static <T extends AttachmentProvider> Optional<T> findAttachment(Player player, File playerDataDir, Class<T> attachmentClass, @Nullable UUID transferFrom) throws IOException {
		Optional<? extends AttachmentProvider> optional = ((PlayerAttachments) player).getPlayerAttachment(attachmentClass);
		if (optional.isPresent())
			return Optional.of((T) optional.get());
		
		File file = new File(playerDataDir, transferFrom != null ? transferFrom.toString() : player.getStringUUID() + "-" + attachmentClass.getSimpleName() + ".dat");
		if (!file.exists() || !file.isFile()) return Optional.empty();
		byte[] data;
		try (FileInputStream fis = new FileInputStream(file)) {
			try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
				fis.transferTo(stream);
				data = stream.toByteArray();
			}
		}
		if (data.length == 0) return Optional.empty();
		try {
			return Optional.ofNullable(SerializationUtil.decompressAndDeserializeObject(data, attachmentClass));
		} catch (Exception e) {
			HmmmLibrary.LOGGER.error("Failed to get player attachment!", e);
			return Optional.empty();
		}
	}
}
