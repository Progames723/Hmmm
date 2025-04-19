package dev.progames723.hmmm.obj_attach;

import dev.progames723.hmmm.HmmmLibrary;
import dev.progames723.hmmm.utils.InternalUtils;
import dev.progames723.hmmm.utils.SerializationUtil;
import net.minecraft.Util;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.List;
import java.util.UUID;

public interface AttachmentProvider {
	static void saveAllAttachments(Player player, File playerDataDir, @Nullable UUID transferFrom) {
		for (AttachmentProvider provider : player.getAttachments()) {
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
		try (InputStream stream = new ByteArrayInputStream(SerializationUtil.serializeObject(attachment))) {
			try (FileOutputStream fos = new FileOutputStream(tempFile)) {
				stream.transferTo(fos);
			}
		}
		
		File file2 = new File(playerDataDir, transferFrom != null ? transferFrom.toString() : player.getStringUUID() + "-" + attachment.getClass().getSimpleName() + ".dat");
		File file3 = new File(playerDataDir, transferFrom != null ? transferFrom.toString() : player.getStringUUID() + "-" + attachment.getClass().getSimpleName() + ".dat_old");
		file2.createNewFile();
		file3.createNewFile();
		Util.safeReplaceFile(file2, tempFile, file3);
	}
	
	static void tryLoadAttachmentsForPlayer(Player player, File playerDataDir, @Nullable UUID transferFrom) {
		List<Class<AttachmentProvider>> classes = InternalUtils.scanClassesForGenerics(AttachmentProvider.class, InternalUtils.ScanType.INTERFACE_IMPL, true);
		if (classes.isEmpty()) HmmmLibrary.LOGGER.info("No attachments found for player {}", player);
		else classes.forEach(attachmentProviderClass -> {
			try {
				AttachmentProvider attachmentProvider = findAttachment(player, playerDataDir, attachmentProviderClass, transferFrom);
				if (attachmentProvider == null) return;
				if (!player.hasAttachment(attachmentProviderClass))
					player.addAttachment(attachmentProvider);
			} catch (IOException e) {
				HmmmLibrary.LOGGER.info("IOException while trying to load attachments!", e);
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	static <T extends AttachmentProvider> T findAttachment(Player player, File playerDataDir, Class<T> attachmentClass, @Nullable UUID transferFrom) throws IOException {
		if (player.hasAttachment(attachmentClass))
			return (T) player.getAttachments().stream().dropWhile(attachmentProvider -> attachmentProvider.getClass() != attachmentClass).findFirst().orElse(null);
		
		File file = new File(playerDataDir, transferFrom != null ? transferFrom.toString() : player.getStringUUID() + "-" + attachmentClass.getSimpleName() + ".dat");
		if (!file.exists() || !file.isFile()) return null;
		byte[] data;
		try (FileInputStream fis = new FileInputStream(file)) {
			try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
				fis.transferTo(stream);
				data = stream.toByteArray();
			}
		}
		if (data == null || data.length == 0) return null;
		try {
			return SerializationUtil.deserializeObject(data, attachmentClass);
		} catch (Exception e) {
			HmmmLibrary.LOGGER.error("Failed to get player attachment!", e);
			return null;
		}
	}
}
