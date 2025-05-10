package dev.progames723.hmmm.obj_attach;

public interface SynchronizedAttachment extends AttachmentProvider {
	/**
	 * this is needed (server -> client)
	 * @return if this attachment can be synchronized to the client
	 */
	boolean canSync();
	
	/**
	 * this is also needed (server <- client)
	 * @return if this attachment can be synchronized back from the client
	 */
	boolean canSyncBack();
}
