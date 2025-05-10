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

package dev.progames723.hmmm.platforms.neoforge.include.net.fabricmc.mappingio.format.tiny;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class Tiny2Util {
	private Tiny2Util() {
	}
	
	public static String unescape(String str) {
		int pos = str.indexOf('\\');
		if (pos < 0) return str;

		StringBuilder ret = new StringBuilder(str.length() - 1);
		int start = 0;

		do {
			ret.append(str, start, pos);
			pos++;
			int type;

			if (pos >= str.length()) {
				throw new RuntimeException("incomplete escape sequence at the end");
			} else if ((type = escaped.indexOf(str.charAt(pos))) < 0) {
				throw new RuntimeException("invalid escape character: \\"+str.charAt(pos));
			} else {
				ret.append(toEscape.charAt(type));
			}

			start = pos + 1;
		} while ((pos = str.indexOf('\\', start)) >= 0);

		ret.append(str, start, str.length());

		return ret.toString();
	}

	private static final String toEscape = "\\\n\r\0\t";
	private static final String escaped = "\\nr0t";

	static final String escapedNamesProperty = "escaped-names";
}
