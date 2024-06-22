package dev.progames723.hmmm.include.net.fabricmc.mappingio.adapter;

import dev.progames723.hmmm.include.net.fabricmc.mappingio.MappedElementKind;
import dev.progames723.hmmm.include.net.fabricmc.mappingio.MappingVisitor;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class FilteringMappingVisitor extends ForwardingMappingVisitor {
	public FilteringMappingVisitor(MappingVisitor next) {
		super(next);
	}
	
	@Override
	public boolean visitMethodArg(int argPosition, int lvIndex, @Nullable String srcName) throws IOException {
		// ignored
		return false;
	}
	
	@Override
	public boolean visitMethodVar(int lvtRowIndex, int lvIndex, int startOpIdx, int endOpIdx, @Nullable String srcName) throws IOException {
		// ignored
		return false;
	}
	
	@Override
	public void visitComment(MappedElementKind targetKind, String comment) throws IOException {
		// ignored
	}
}
