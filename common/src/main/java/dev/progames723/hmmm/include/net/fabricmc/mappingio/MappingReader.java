/*
 * Copyright (c) 2021 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.progames723.hmmm.include.net.fabricmc.mappingio;

import dev.progames723.hmmm.include.net.fabricmc.mappingio.format.MappingFormat;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public final class MappingReader {
	private MappingReader() {
	}
	
	@Nullable
	public static MappingFormat detectFormat(Reader reader) throws IOException {
		return detectFormat(reader, null);
	}

	private static MappingFormat detectFormat(Reader reader, @Nullable String fileExt) throws IOException {
		char[] buffer = new char[DETECT_HEADER_LEN];
		int pos = 0;
		int len;

		// Be careful not to close the reader, that's up to the caller.
		BufferedReader br = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);

		br.mark(DETECT_HEADER_LEN);

		while (pos < buffer.length
				&& (len = br.read(buffer, pos, buffer.length - pos)) >= 0) {
			pos += len;
		}

		br.reset();
		if (pos < 3) return null;

		switch (String.valueOf(buffer, 0, 3)) {
		case "v1\t":
			return MappingFormat.TINY_FILE;
		case "tin":
			return MappingFormat.TINY_2_FILE;
		case "tsr": // tsrg2 <nsA> <nsB> ..<nsN>
			return MappingFormat.TSRG_2_FILE;
		case "CLA":
			return MappingFormat.ENIGMA_FILE;
		case "PK:":
		case "CL:":
		case "FD:":
		case "MD:":
			return detectSrgOrXsrg(br, fileExt);
		case "CL ":
		case "FD ":
		case "MD ":
		case "MP ":
			return MappingFormat.JAM_FILE;
		}

		String headerStr = String.valueOf(buffer, 0, pos);

		if ((headerStr.startsWith("p ")
				|| headerStr.startsWith("c ")
				|| headerStr.startsWith("f ")
				|| headerStr.startsWith("m "))
				&& headerStr.contains(" = ")) {
			return MappingFormat.JOBF_FILE;
		} else if (headerStr.contains(" -> ")) {
			return MappingFormat.PROGUARD_FILE;
		} else if (headerStr.contains("\n\t")) {
			return MappingFormat.TSRG_FILE;
		}

		if (fileExt != null) {
			if (fileExt.equals(MappingFormat.CSRG_FILE.fileExt)) return MappingFormat.CSRG_FILE;
		}

		// TODO: Recaf Simple

		return null; // format unknown, not easily detectable or corrupted
	}

	private static MappingFormat detectSrgOrXsrg(BufferedReader reader, @Nullable String fileExt) throws IOException {
		String line;

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("FD:")) {
				String[] parts = line.split(" ");

				if (parts.length < 5
						|| isEmptyOrStartsWithHash(parts[3])
						|| isEmptyOrStartsWithHash(parts[4])) {
					return MappingFormat.SRG_FILE;
				}

				return MappingFormat.XSRG_FILE;
			}
		}

		return MappingFormat.XSRG_FILE.fileExt.equals(fileExt) ? MappingFormat.XSRG_FILE : MappingFormat.SRG_FILE;
	}

	private static boolean isEmptyOrStartsWithHash(String string) {
		return string.isEmpty() || string.startsWith("#");
	}
	
	private static final int DETECT_HEADER_LEN = 4096;
}
