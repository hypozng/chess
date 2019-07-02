package priv.hypo.chess.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 字符串工具类
 * @author hypo
 */
public class StringUtil {
	
	public static final String SEPARATOR = ",";

	/**
	 * 将字符串数组中的成员根据指定的分隔符链接乘一个新的字符串
	 * @param separator 分隔符
	 * @param array 需要链接的字符串数组
	 * @return 链接后的字符串
	 */
	public static String join(String separator, String...elements) {
		if(elements == null) {
			return null;
		}
		StringBuffer text = new StringBuffer();
		boolean started = false;
		for(String element : elements) {
			if(started) {
				text.append(separator);
			} else {
				started = true;
			}
			text.append(element);
		}
		return text.toString();
	}
	
	public static String join(String...elements) {
		return join(SEPARATOR, elements);
	}
	
	/**
	 * 将集合中的成员链接成一个新的字符串
	 * @param separator 分隔符
	 * @param elements 集合
	 * @return 链接后的字符串
	 */
	public static String join(String separator, Collection<?> elements) {
		if(elements == null) {
			return null;
		}
		String[] array = elements.toArray(new String[elements.size()]);
		return join(separator, array);
	}
	
	/**
	 * 将集合中的成员使用","分割链接成一个新的字符串
	 * @param elements 集合
	 * @return 链接后的字符串
	 */
	public static String join(Collection<?> elements) {
		return join(SEPARATOR, elements);
	}
	
	/**
	 * 将map中的键值对以指定的分隔符链接为字符串
	 * @param map 
	 * @return 链接后的字符串
	 */
	public static String join(String separator, Map<?, ?> map) {
		if(map == null) {
			return null;
		}
		List<String> entries = new ArrayList<String>();
		for(Map.Entry<?, ?> entry : map.entrySet()) {
			entries.add(entry.toString());
		}
		return join(separator, entries);
	}
	
	/**
	 * 将map中的键值对以','作为分隔符链接为新的字符串
	 * @param map
	 * @return 链接后的字符串
	 */
	public static String join(Map<?, ?> map) {
		return join(SEPARATOR, map);
	}
	
	/**
	 * 分割字符串
	 * @param source 需要分割的字符串
	 * @param regex 分隔符(支持正则表达式)，若传入的值为null，则以','分割
	 * @return 分割后的字符串数组
	 */
	public static String[] split(String source, String regex) {
		if(source == null) {
			return null;
		}
		if(regex == null) {
			regex = "[,]";
		}
		return source.split(regex);
	}
	
	/**
	 * 以','作为分隔符分割字符串
	 * @param source 需要分割的字符串
	 * @return 分割后的字符串
	 */
	public static String[] split(String source) {
		return split(source, null);
	}
	
	/**
	 * 将字符串分割为list
	 * @param source 需要分割的字符串
	 * @param regex 分隔符
	 * @return 分割后的字符串
	 */
	public static List<String> splitAsList(String source, String regex) {
		if(source == null) {
			return null;
		}
		String[] array = split(source, regex);
		return Arrays.asList(array);
	}
	
	/**
	 * 以','作为分隔符将字符串分割为list
	 * @param source 需要分割的字符串
	 * @return 分割后的字符串
	 */
	public static List<String> splitAsList(String source) {
		return splitAsList(source, null);
	}
	
	/**
	 * 将包含键和值得字符串中的键和值分割开
	 * @param source 包含键和值的字符串
	 * @param separator 分隔符
	 * @return 包含键和值的字符串数组，数组的第一个成员是键，第二个元素是值
	 */
	public static String[] splitKeyAndValue(String source, String separator) {
		if(source == null) {
			return null;
		}
		if(separator == null) {
			separator = "=";
		}
		int splitIndex = source.indexOf(separator);
		if(splitIndex == -1) {
			return new String[] {source, null};
		}
		String key = source.substring(0, splitIndex);
		String value = source.substring(splitIndex + 1);
		return new String[] {key, value};
	}
	
	/**
	 * 将键和值的字符串中的键和值以'='分隔开
	 * @param source 包含键和值的字符串
	 * @return 包含键和值的字符串数组，数组的第一个成员是键，第二个成员是值
	 */
	public static String[] splitKeyAndValue(String source) {
		return splitKeyAndValue(source, null);
	}
	
	/**
	 * 将字符串分割为map
	 * @param source 需要分割的字符串
	 * @param entryRegex 用来分割键值对的分隔符
	 * @param valueSeparator 用来分割键和值的分隔符
	 * @return 分割后的map
	 */
	public static Map<String, String> splitAsMap(String source, String entryRegex, String valueSeparator) {
		if(source == null) {
			return null;
		}
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		String[] entries = split(source, entryRegex);
		for(String entry : entries) {
			String[] data = splitKeyAndValue(entry, valueSeparator);
			map.put(data[0], data[1]);
		}
		return map;
	}
	
	/**
	 * 将字符串分割为map，键和值的分隔符采用'='
	 * @param source 需要分割的字符串
	 * @param entryRegex 键值对的分隔符
	 * @return 分割后的map
	 */
	public static Map<String, String> splitAsMap(String source, String entryRegex) {
		return splitAsMap(source, entryRegex, null);
	}
	
	/**
	 * 将字符串分割为map，键值对的分隔符采用','，键和值的分隔符采用'='
	 * @param source 需要分割的字符串
	 * @return 分割后的map
	 */
	public static Map<String, String> splitAsMap(String source) {
		return splitAsMap(source, null, null);
	}
	
	/**
	 * 生成一个uuid
	 * @return
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("[\\-]", "");
	}
}