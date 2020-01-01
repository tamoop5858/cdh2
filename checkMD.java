package Translation;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class checkMD {

	public static void main(String[] args) {

		String basePath = "E:\\share\\99_others\\suwabe\\pleiades\\workspace\\fd-rawstore\\src\\main\\java\\Translation\\";

		String PROP_FILE = basePath + "china-japan.txt";

		doSingleCheck(PROP_FILE);
		System.out.println( " ------- single check ------ " );
		System.out.println( "" );

		try {
            //文字コードUTF-8を指定してファイルを読み込む
            FileInputStream input = new FileInputStream(PROP_FILE);
            InputStreamReader stream = new InputStreamReader(input,"UTF-8");
            BufferedReader buffer = new BufferedReader(stream);

            String str;
            int i=1;

            //<table> <tr> <th> <td> <li>
            String[] chars = {"#", "*","|", "-", ">", "【", "{"};

          //<table> <tr> <th> <td> <li>
            String[] keywords = {"<table>", "<tr>",  "<th>",  "<td>",  "<li>","</table>", "<b>", "</b>",
            		"</tr>",  "</th>",  "</td>",  "</li>", "</a>", ">-"};

            Map<String, String> china_japan_map = new HashMap<String, String>();
            Map<String, String> japan_china_map = new HashMap<String, String>();

            //ファイルの最終行まで読み込む
            while((str = buffer.readLine()) != null){
                //文字コードをShift-JISに変換する
                //str = new String(b, "Shift-JIS");

                String[] col = str.split("\t", -1);
                String china = col[0].trim();
                String japan = col[1].trim();

                //3.0 中国語が同じ、日本語が違い
                if (china_japan_map.containsKey(china) ) {
                	if (!japan.equals(china_japan_map.get(china))) {
                		System.out.println("中国語が同じ、日本語が違い " + i + "行: "  + str);
                	}
                }
                china_japan_map.put(china, japan);

                //4.0 日本語が同じ、中国語が違い
                if (japan_china_map.containsKey(japan) ) {
                	if (!china.equals(japan_china_map.get(japan))) {
                		System.out.println("日本語が同じ、中国語が違い " + i + "行: "  + str);
                	}
                }
                japan_china_map.put(japan, china);

                // # * |
                for (String c : chars) {
                    //3.0 |の数のチェック
                    if (str.indexOf(c) >= 0) {
                    	int chian_count = regex(china, c);
                    	int japan_count = regex(japan, c);
                    	if ( chian_count != japan_count) {
                    		System.out.println(c + " -->の数不正 " + i + "行: " + chian_count + "と" + japan_count + " " + str);
                    	}
                    }
                }

                //html
                for (String keyword : keywords) {
                	String chian_replace = china.replace(keyword, "@");
                	String japan_replace = japan.replace(keyword, "@");
                    //3.0 |の数のチェック
                    if (chian_replace.indexOf("@") >= 0) {
                    	int chian_count = regex(chian_replace, "@");
                    	int japan_count = regex(japan_replace, "@");
                    	if ( chian_count != japan_count) {
                    		System.out.println(keyword + " -->の数不正 " + i + "行: " + chian_count + "と" + japan_count + " " + str);
                    	}
                    }
                }


                //1.0 |の場合、空白の数のチェック
                if (str.indexOf("|") >= 0) {
                	int chian_count = regex(china, " ");
                	int japan_count = regex(japan, " ");
                	if ( chian_count != japan_count) {
                		System.out.println("|の空白数不正 " + i + "行: " + chian_count + "と" + japan_count + " " + str);
                	}

                }

                //2.0 全て半角の場合、同じかのチェック
                if (isHankaku(china) ) {
                	if (!china.equals(japan)) {
                		System.out.println("Htmlのcodeが不一致 " + i + "行: "  + str);
                	}
                }

                //3.0　全角のチェック
                if (china.indexOf("http") >= 0 ) {
                	//System.out.println("httpあり " + i + "行: "  + china);
                	//System.out.println("httpあり " + i + "行: "  + japan);
                }

                String chianHankakuOnly = deleteZenkaku(china);
                String japanHankakuOnly = deleteZenkaku(japan);
                if (!chianHankakuOnly.equals(japanHankakuOnly)) {
                	System.out.println("全角を除いた半角が不正 " + i + "行: "  + chianHankakuOnly + "-->" + japanHankakuOnly);
                }
                i++;
            }

            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	static int regex(String str, String target){
		int count = 0;
		int size = str.length();
		for( int i = 0; i < size; i++ )
        {
            String s = str.substring(i, i+1);

            if( s.equals( target ) )
                count++;
        }

		return count;
	}

	static void doSingleCheck(String PROP_FILE){
		try {
            //文字コードUTF-8を指定してファイルを読み込む
            FileInputStream input = new FileInputStream(PROP_FILE);
            InputStreamReader stream = new InputStreamReader(input,"UTF-8");
            BufferedReader buffer = new BufferedReader(stream);

            String str;
            int i=1;


            //ファイルの最終行まで読み込む
            while((str = buffer.readLine()) != null){
                //文字コードをShift-JISに変換する
                //str = new String(b, "Shift-JIS");

                String[] col = str.split("\t", -1);
                String chian = col[0].trim();
                String japan = col[1].trim();

            	//1. ,のチェック、中国語カンマのチェック
            	if (japan.indexOf("，") > 0) {
            		System.out.println( "中国語カンマがある！ " + i + "行: " + japan);
            	}

            	//2. []()のチェック
            	if (japan.indexOf("]") >= 0 ) {
            		if (japan.indexOf("](") < 0) {
            			System.out.println( "[]()離れている！ " + i + "行: " + japan);
            		}
            	}

            	//3. *の後ろが空白のチェック
            	if (japan.indexOf("* ") >= 0 ) {
            		System.out.println( "*の後ろ空白がある！ " + i + "行: " + japan);
            	}

            	//4. 全角空白のチェック
            	if (japan.indexOf("　") >= 0 ) {
            		System.out.println( "全角空白がある！ " + i + "行: " + japan);
            	}

                i++;
            }

            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

    static boolean isHankaku(String s) {
    	char[] chars = s.toCharArray();
    	for (int i = 0; i < chars.length; i++) {
    		if (String.valueOf(chars[i]).getBytes().length >= 2) {
    			return false;
    		}
    	}
    	return true;
    }

    static String deleteZenkaku(String s) {
    	char[] chars = s.toCharArray();
    	StringBuffer sb = new StringBuffer();
    	for (int i = 0; i < chars.length; i++) {
    		if (String.valueOf(chars[i]).getBytes().length >= 2) {
    			continue;
    		}

    		if (chars[i]==' ') {
    			continue;
    		}
    		sb.append(chars[i]);
    	}
    	return sb.toString();
    }
}
