package org.kframework.utils;

public class StringUtil {
	public static String unescape(String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\\') {
				if (str.charAt(i + 1) == '\\')
					sb.append('\\');
				else if (str.charAt(i + 1) == 'n')
					sb.append('\n');
				else if (str.charAt(i + 1) == 'r')
					sb.append('\r');
				else if (str.charAt(i + 1) == 't')
					sb.append('\t');
				else if (str.charAt(i + 1) == '"')
					sb.append('"');
				i++;
			} else
				sb.append(str.charAt(i));
		}

		return sb.toString();
	}

	public static String makeProper(String str) {
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}

	public static String escape(String value) {
        final int length = value.length();
        StringBuilder result = new StringBuilder();
        for (int offset = 0, codepoint; offset < length; offset += Character.charCount(codepoint)) {
            codepoint = value.codePointAt(offset);
            if (codepoint == '"') {
                result.append("\\\"");
            } else if (codepoint == '\\') {
                result.append("\\\\");
            } else if (codepoint == '\n') {
                result.append("\\n");
            } else if (codepoint == '\t') {
                result.append("\\t");
            } else if (codepoint == '\r') {
                result.append("\\r");
            } else if (codepoint >= 32 && codepoint < 127) {
                result.append((char)codepoint);
            } else if (codepoint <= 0xff) {
                result.append("\\");
                result.append(String.format("%03o", codepoint));
            }
        }
        return result.toString();
	}

    public static void throwIfSurrogatePair(int codePoint) {
        if (codePoint >= 0xd800 && codePoint <= 0xdfff) {
            //we are trying to encode a surrogate pair, which the unicode
            //standard forbids
            throw new IllegalArgumentException();
        }
    }

    public static int lastIndexOfAny(String str, String search, int offset) {
        if (str.equals("") || search.equals("")) {
            return -1;
        }
        for (int i = str.length(), strCodepoint; i > 0; i -= Character.charCount(strCodepoint)) {
            strCodepoint = str.codePointBefore(i);
            for (int j = search.length(), searchCodepoint; j > 0; j -= Character.charCount(searchCodepoint)) {
                searchCodepoint = search.codePointBefore(j);
                if (strCodepoint == searchCodepoint) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static int indexOfAny(String str, String search, int offset) {
        if (str.equals("") || search.equals("")) {
            return -1;
        }
        for (int i = 0, strCodepoint; i < str.length(); i += Character.charCount(strCodepoint)) {
            strCodepoint = str.codePointAt(i);
            for (int j = 0, searchCodepoint; j < search.length(); j += Character.charCount(searchCodepoint)) {
                searchCodepoint = search.codePointAt(j);
                if (strCodepoint == searchCodepoint) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static String unescapeK(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < str.length() - 1; i++) {
			if (str.charAt(i) == '\\') {
			    if (str.charAt(i + 1) == '"') {
			        sb.append('"');
                    i++;
    			} else if (str.charAt(i + 1) == '\\') {
	    			sb.append('\\');
                    i++;
		    	} else if (str.charAt(i + 1) == 'n') {
			    	sb.append('\n');
                    i++;
                } else if (str.charAt(i + 1) == 'r') {
                    sb.append('\r');
                    i++;
                } else if (str.charAt(i + 1) == 't') {
                    sb.append('\t');
                    i++;
                } else if (str.charAt(i + 1) == 'x') {
                    String arg = str.substring(i + 2, i + 4);
                    sb.append((char)Integer.parseInt(arg, 16));
                    i += 3;
                } else if (str.charAt(i + 1) == 'u') {
                    String arg = str.substring(i + 2, i + 6);
                    int codePoint = Integer.parseInt(arg, 16);
                    StringUtil.throwIfSurrogatePair(codePoint);
                    sb.append((char)codePoint);
                    i += 5;
                } else if (str.charAt(i + 1) == 'U') {
                    String arg = str.substring(i + 2, i + 10);
                    int codePoint = Integer.parseInt(arg, 16);
                    StringUtil.throwIfSurrogatePair(codePoint);
                    sb.append(Character.toChars(codePoint));
                    i += 9;
                }
            } else {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

	
    public static String escapeK(String value) {
        final int length = value.length();
        StringBuilder result = new StringBuilder();
        result.append("\"");
        for (int offset = 0, codepoint; offset < length; offset += Character.charCount(codepoint)) {
            codepoint = value.codePointAt(offset);
            if (codepoint == '"') {
                result.append("\\\"");
            } else if (codepoint == '\\') {
                result.append("\\\\");
            } else if (codepoint == '\n') {
                result.append("\\n");
            } else if (codepoint == '\t') {
                result.append("\\t");
            } else if (codepoint == '\r') {
                result.append("\\r");
            } else if (codepoint >= 32 && codepoint < 127) {
                result.append((char)codepoint);
            } else if (codepoint <= 0xff) {
                result.append("\\x");
                result.append(String.format("%02x", codepoint));
            } else if (codepoint <= 0xffff) {
                result.append("\\u");
                result.append(String.format("%04x", codepoint));
            } else {
                result.append("\\U");
                result.append(String.format("%08x", codepoint));
            }
        }
        result.append("\"");
        return result.toString();
    }

	/**
	 * Use this function to print XML directly as string, and not when using DOM.
	 * 
	 * @param str
	 * @return
	 *

	public static String escapeToXmlAttribute(String str) {
		str = str.replaceAll("\\", "\\\\");
		str = str.replaceAll("\n", "\\n");
		str = str.replaceAll("\r", "\\r");
		str = str.replaceAll("\t", "\\t");
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("\"", "&quot;");
		return str;
	}

    /**
     * Returns the two-letter code for a general category of Unicode code point.
     */
    public static String getCategoryCode(byte cat) {
        switch(cat) {
            case Character.COMBINING_SPACING_MARK:
                return "Mc";
            case Character.CONNECTOR_PUNCTUATION:
                return "Pc";
            case Character.CONTROL:
                return "Cc";
            case Character.CURRENCY_SYMBOL:
                return "Sc";
            case Character.DASH_PUNCTUATION:
                return "Pd";
            case Character.DECIMAL_DIGIT_NUMBER:
                return "Nd";
            case Character.ENCLOSING_MARK:
                return "Me";
            case Character.END_PUNCTUATION:
                return "Pe";
            case Character.FINAL_QUOTE_PUNCTUATION:
                return "Pf";
            case Character.FORMAT:
                return "Cf";
            case Character.INITIAL_QUOTE_PUNCTUATION:
                return "Pi";
            case Character.LETTER_NUMBER:
                return "Nl";
            case Character.LINE_SEPARATOR:
                return "Zl";
            case Character.LOWERCASE_LETTER:
                return "Ll";
            case Character.MATH_SYMBOL:
                return "Sm";
            case Character.MODIFIER_LETTER:
                return "Lm";
            case Character.MODIFIER_SYMBOL:
                return "Sk";
            case Character.NON_SPACING_MARK:
                return "Mn";
            case Character.OTHER_LETTER:
                return "Lo";
            case Character.OTHER_NUMBER:
                return "No";
            case Character.OTHER_PUNCTUATION:
                return "Po";
            case Character.OTHER_SYMBOL:
                return "So";
            case Character.PARAGRAPH_SEPARATOR:
                return "Zp";
            case Character.PRIVATE_USE:
                return "Co";
            case Character.SPACE_SEPARATOR:
                return "Zs";
            case Character.START_PUNCTUATION:
                return "Ps";
            case Character.SURROGATE:
                return "Cs";
            case Character.TITLECASE_LETTER:
                return "Lt";
            case Character.UNASSIGNED:
                return "Cn";
            case Character.UPPERCASE_LETTER:
                return "Lu";
            default:
                assert false: "should be exhaustive list of categories";
                return null; //unreachable
        }
    }

    public static String getDirectionalityCode(byte cat) {
        switch(cat) {
            case Character.DIRECTIONALITY_ARABIC_NUMBER:
                return "AN";
            case Character.DIRECTIONALITY_BOUNDARY_NEUTRAL:
                return "BN";
            case Character.DIRECTIONALITY_COMMON_NUMBER_SEPARATOR:
                return "CS";
            case Character.DIRECTIONALITY_EUROPEAN_NUMBER:
                return "EN";
            case Character.DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR:
                return "ES";
            case Character.DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR:
                return "ET";
            case Character.DIRECTIONALITY_LEFT_TO_RIGHT:
                return "L";
            case Character.DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING:
                return "LRE";
            case Character.DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE:
                return "LRO";
            case Character.DIRECTIONALITY_NONSPACING_MARK:
                return "NSM";
            case Character.DIRECTIONALITY_OTHER_NEUTRALS:
                return "ON";
            case Character.DIRECTIONALITY_PARAGRAPH_SEPARATOR:
                return "B";
            case Character.DIRECTIONALITY_POP_DIRECTIONAL_FORMAT:
                return "PDF";
            case Character.DIRECTIONALITY_RIGHT_TO_LEFT:
                return "R";
            case Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC:
                return "AL";
            case Character.DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING:
                return "RLE";
            case Character.DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE:
                return "RLO";
            case Character.DIRECTIONALITY_SEGMENT_SEPARATOR:
                return "S";
            case Character.DIRECTIONALITY_UNDEFINED:
                throw new IllegalArgumentException();
            case Character.DIRECTIONALITY_WHITESPACE:
                return "WS";
            default:
                assert false: "should be exhaustive list of directionalities";
                return null; //unreachable
        }
    }

	public static String escapeSortName(String str) {
		str = str.replace("D", "Dd");
		str = str.replace("#", "Dz");
		str = str.replace("{", "Dl");
		str = str.replace("}", "Dr");
		return str;
	}

	public static String unEscapeSortName(String str) {
		str = str.replace("Dl", "{");
		str = str.replace("Dr", "}");
		str = str.replace("Dz", "#");
		str = str.replace("Dd", "D");
		return str;
	}

	public static String getSortNameFromCons(String str) {
		String ret = "";
		int idx = str.lastIndexOf("1");

		if (idx > 0) {
			ret = str.substring(0, idx);
		}
		return StringUtil.unEscapeSortName(ret);
	}

	private static int number = 0;

	/**
	 * Generate incremental numbers that dosn't contain the number 1
	 * 
	 * @return an integer that doesn't contain the number 1
	 */
	public static int getUniqueId() {
		boolean valid = false;
		while (!valid) {
			int nr = number;
			while (nr > 0) {
				if (nr % 10 == 1) {
					number++;
					break;
				} else {
					nr /= 10;
				}
			}
			if (nr == 0) {
				valid = true;
			}
		}
		return number++;
	}

	public static String escapeMaude(String tag) {
		// TODO [andreis]: current implementation appears wrong to me, i.e. '`(`) stays the same rather than becoming '```(```)
		tag = tag.replaceAll("(?<!`)`", "BKQT");
		return tag.replaceAll("(?<!`)([\\(\\)\\[\\]\\{\\},])", "`$1");
	}

	public static String unescapeMaude(String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '`') {
				if (str.charAt(i + 1) == '`')
					sb.append('`');
				else if (str.charAt(i + 1) == '(')
					sb.append('(');
				else if (str.charAt(i + 1) == ')')
					sb.append(')');
				else if (str.charAt(i + 1) == '[')
					sb.append('[');
				else if (str.charAt(i + 1) == ']')
					sb.append(']');
				else if (str.charAt(i + 1) == '{')
					sb.append('{');
				else if (str.charAt(i + 1) == '}')
					sb.append('}');
				else if (str.charAt(i + 1) == ',')
					sb.append(',');
				else
					sb.append(' ');
				i++;
			} else {
				if (str.charAt(i) == 'B' && str.charAt(i + 1) == 'K' && str.charAt(i + 2) == 'Q' && str.charAt(i + 3) == 'T') {
					sb.append('`');
					i += 3;
				} else {
					sb.append(str.charAt(i));
				}
			}
		}

		return sb.toString();
	}

	/**
	 * This function removes space when declaring equations in lists: -$ cat m.maude mod M is sort S . ops a b c : -> S . op _ _ : S S -> S . eq __(a, b) = c . endm red a b . q -$ maude m.maude
	 * \||||||||||||||||||/ --- Welcome to Maude --- /||||||||||||||||||\ Maude 2.6 built: Dec 10 2010 11:12:39 Copyright 1997-2010 SRI International Sun Aug 26 11:01:21 2012
	 * ========================================== reduce in M : a b . rewrites: 1 in 0ms cpu (0ms real) (1000000 rewrites/second) result S: c Bye. -$
	 * 
	 * @param tag
	 * @return
	 */
	public static String equationSpaceElimination(String tag) {
		return tag.replaceAll("\\s", "");
	}

	public static String latexify(String name) {
		return name.replace("\\", "\\textbackslash ").replace("_", "\\_").replace("{", "\\{").replace("}", "\\}").replace("#", "\\#").replace("%", "\\%").replace("$", "\\$")
				.replace("&", "\\&").replace("~", "\\mbox{\\~{}}").replace("^", "\\mbox{\\^{}}").replace("`", "\\mbox{\\`{}}");
	}

	public static String emptyIfNull(String string) {
		if (string == null)
			return "";
		return string;
	}

	public static int getStartLineFromLocation(String location) {
		String[] str = location.split("[\\(,\\)]");
		return Integer.parseInt(str[0 + 1]);
	}

	public static int getStartColFromLocation(String location) {
		String[] str = location.split("[\\(,\\)]");
		return Integer.parseInt(str[1 + 1]);
	}
}
