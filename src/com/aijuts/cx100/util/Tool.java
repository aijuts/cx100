package com.aijuts.cx100.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.aijuts.cx100.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class Tool {
	
	private Context context;
	
	public Tool(Context context) {
		this.context = context;
	}
	
	public final double PI = 3.14159265;
	public final double RAD = Math.PI / 180.0;
	public final double EARTH_RADIUS = 6378137;  //地球半径
	public final int IO_BUFFER_SIZE = 4 * 1024;
	
	/** 
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米 
     * @param lng1 
     * @param lat1 
     * @param lng2 
     * @param lat2 
     * @return 
     */  
    public double getDistance(double lat1, double lng1, double lat2, double lng2)
    {  
       double radLat1 = lat1*RAD;  
       double radLat2 = lat2*RAD;  
       double a = radLat1 - radLat2;  
       double b = (lng1 - lng2)*RAD;  
       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +  
        Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));  
       s = s * EARTH_RADIUS;  
       s = Math.round(s * 10000) / 10000;  
       return s;  
    }
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * 获取服务器json数据
	 * @param url
	 * @return
	 */
	public static String doGet(String url) {
		try {
			String result = null;
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
			HttpResponse response = httpClient.execute(request);
			result = EntityUtils.toString(response.getEntity());
			return result;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	public static RestTemplate geTemplate() {
		RestTemplate template = new RestTemplate();
		template.getMessageConverters().add(new StringHttpMessageConverter());
		return template;
	}
	
    /**
     * 转换图片成圆形
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public Bitmap toRoundBitmap(Bitmap bitmap) {
    	int width = bitmap.getWidth();
    	int height = bitmap.getHeight();
    	float roundPx;
    	float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
    	if (width <= height) {
    		roundPx = width / 2;
    		top = 0;
    		bottom = width;
    		left = 0;
    		right = width;
    		height = width;
    		dst_left = 0;
    		dst_top = 0;
    		dst_right = width;
    		dst_bottom = width;
    	} else {
    		roundPx = height / 2;
    		float clip = (width - height) / 2;
    		left = clip;
    		right = width - clip;
    		top = 0;
    		bottom = height;
    		width = height;
    		dst_left = 0;
    		dst_top = 0;
    		dst_right = height;
    		dst_bottom = height;
    	}
    	
    	Bitmap output = Bitmap.createBitmap(width,
    			height, Config.ARGB_8888);
    	Canvas canvas = new Canvas(output);
    	
    	final int color = 0xff424242;
    	final Paint paint = new Paint();
    	final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
    	final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
    	final RectF rectF = new RectF(dst);
    	
    	paint.setAntiAlias(true);
             
    	canvas.drawARGB(0, 0, 0, 0);
    	paint.setColor(color);
    	canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

    	paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
    	canvas.drawBitmap(bitmap, src, dst, paint);
    	return output;
    }
    
    /**
     * Resources转化为Bitmap
     * @param id
     * @return
     */
    public Bitmap Resources2Bitmap(int id) {
    	Resources res = context.getResources();
		Bitmap bmp = BitmapFactory.decodeResource(res, id);
		return bmp;
	}
    
    /**
     * url - 网络或者本地图片的绝对路径,比如:
		A.网络路径: url="http://blog.foreverlove.us/girl2.png"  ;
		B.本地路径:url="file://mnt/sdcard/photo/image.png";
		C.支持的图片格式 ,png, jpg,bmp,gif等等
     * @param url
     * @return
     */
    public Bitmap GetNetBitmap(String url) {
    	Bitmap bitmap = null;
    	InputStream in = null;
        BufferedOutputStream out = null;
        try {
        	in = new BufferedInputStream(new URL(url).openStream(),
        			IO_BUFFER_SIZE);
        	final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        	out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
        	copy(in, out);
        	out.flush();
        	byte[] data = dataStream.toByteArray();
        	bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        	data = null;
        	return bitmap;
        } catch (IOException e) {
        	e.printStackTrace();
        	return null;
        }
    }

    public void copy(InputStream in, OutputStream out) throws IOException {
    	byte[] b = new byte[IO_BUFFER_SIZE];
    	int read;
    	while ((read = in.read(b)) != -1) {
    		out.write(b, 0, read);
    	}
    }
    
    /**
     * 四舍五入保留两位小数
     * @param a
     * @return
     */
    public double saveDistance(double a) {
    	int b = (int)Math.round(a * 100); //小数点后两位前移，并四舍五入 
		double c = (double)b / 100.00; //还原小数点后两位
		return c;
    }
    
    private char[] hexDigit = {
    		'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
    };
   
    private char toHex(int nibble) {
    	return hexDigit[(nibble & 0xF)];
    }
   
    /**
     * 将字符串编码成 Unicode 。
     * @param theString 待转换成Unicode编码的字符串。
     * @param escapeSpace 是否忽略空格。
     * @return 返回转换后Unicode编码的字符串。
     */
    public String toUnicode(String theString, boolean escapeSpace) {
    	int len = theString.length();
    	int bufLen = len * 2;
    	if (bufLen < 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuffer outBuffer = new StringBuffer(bufLen);

        for(int x=0; x<len; x++) {
            char aChar = theString.charAt(x);
            // Handle common case first, selecting largest block that
            // avoids the specials below
            if ((aChar > 61) && (aChar < 127)) {
                if (aChar == '\\') {
                    outBuffer.append('\\'); outBuffer.append('\\');
                    continue;
                }
                outBuffer.append(aChar);
                continue;
            }
            switch(aChar) {
                case ' ':
                    if (x == 0 || escapeSpace) 
                        outBuffer.append('\\');
                    outBuffer.append(' ');
                    break;
                case '\t':outBuffer.append('\\'); outBuffer.append('t');
                          break;
                case '\n':outBuffer.append('\\'); outBuffer.append('n');
                          break;
                case '\r':outBuffer.append('\\'); outBuffer.append('r');
                          break;
                case '\f':outBuffer.append('\\'); outBuffer.append('f');
                          break;
                case '=': // Fall through
                case ':': // Fall through
                case '#': // Fall through
                case '!':
                    outBuffer.append('\\'); outBuffer.append(aChar);
                    break;
                default:
                    if ((aChar < 0x0020) || (aChar > 0x007e)) {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(toHex((aChar >> 12) & 0xF));
                        outBuffer.append(toHex((aChar >>   8) & 0xF));
                        outBuffer.append(toHex((aChar >>   4) & 0xF));
                        outBuffer.append(toHex( aChar         & 0xF));
                    } else {
                        outBuffer.append(aChar);
                    }
            }
        }
        return outBuffer.toString();
    }
   
    /**
     * 从 Unicode 码转换成编码前的特殊字符串。
     * @param in Unicode编码的字符数组。
     * @param off 转换的起始偏移量。
     * @param len 转换的字符长度。
     * @param convtBuf 转换的缓存字符数组。
     * @return 完成转换，返回编码前的特殊字符串。
     */
    public String fromUnicode(char[] in, int off, int len, char[] convtBuf) {
        if (convtBuf.length < len) {
            int newLen = len * 2;
            if (newLen < 0) {
                newLen = Integer.MAX_VALUE;
            }
            convtBuf = new char[newLen];
        }
        char aChar;
        char[] out = convtBuf;
        int outLen = 0;
        int end = off + len;

        while (off < end) {
            aChar = in[off++];
            if (aChar == '\\') {
                aChar = in[off++];
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = in[off++];
                        switch (aChar) {
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            value = (value << 4) + aChar - '0';
                            break;
                        case 'a':
                        case 'b':
                        case 'c':
                        case 'd':
                        case 'e':
                        case 'f':
                            value = (value << 4) + 10 + aChar - 'a';
                            break;
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                            value = (value << 4) + 10 + aChar - 'A';
                            break;
                        default:
                            throw new IllegalArgumentException(
                                    "Malformed \\uxxxx encoding.");
                        }
                    }
                    out[outLen++] = (char) value;
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    out[outLen++] = aChar;
                }
            } else {
                out[outLen++] = (char) aChar;
            }
        }
        return new String(out, 0, outLen);
    }
    
    public String gbEncoding(final String gbString) {  
        char[] utfBytes = gbString.toCharArray();  
        String unicodeBytes = "";  
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {  
            String hexB = Integer.toHexString(utfBytes[byteIndex]);  
            if (hexB.length() <= 2) {  
                hexB = "00" + hexB;  
            }  
            unicodeBytes = unicodeBytes + "\\u" + hexB;  
        }  
        System.out.println("unicodeBytes is: " + unicodeBytes);  
        return unicodeBytes;  
    }  
  
    public String decodeUnicode(final String dataStr) {  
        int start = 0;  
        int end = 0;  
        final StringBuffer buffer = new StringBuffer();  
        while (start > -1) {  
            end = dataStr.indexOf("\\u", start + 2);  
            String charStr = "";  
            if (end == -1) {  
                charStr = dataStr.substring(start + 2, dataStr.length());  
            } else {  
                charStr = dataStr.substring(start + 2, end);  
            }  
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。  
            buffer.append(new Character(letter).toString());  
            start = end;  
        }  
        return buffer.toString();  
    }
    
    /* 
     * 16进制数字字符集 
     */ 
    private String hexString="0123456789abcdef"; 
    
    /* 
     * 将字符串编码成16进制数字,适用于所有字符（包括中文） 
     */ 
    public String encode(String str) 
    { 
    	//根据默认编码获取字节数组 
    	byte[] bytes=str.getBytes(); 
    	StringBuilder sb=new StringBuilder(bytes.length*2); 
    	//将字节数组中每个字节拆解成2位16进制整数 
    	for(int i=0;i<bytes.length;i++) 
    	{ 
    		sb.append(hexString.charAt((bytes[i]&0xf0)>>4)); 
    		sb.append(hexString.charAt((bytes[i]&0x0f)>>0)); 
    	} 
    	return sb.toString(); 
    } 
    
    /* 
     * 将16进制数字解码成字符串,适用于所有字符（包括中文） 
     */ 
    public String decode(String bytes) 
    { 
    	ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2); 
    	//将每2位16进制整数组装成一个字节 
    	for(int i=0;i<bytes.length();i+=2) 
    		baos.write((hexString.indexOf(bytes.charAt(i))<<4 |hexString.indexOf(bytes.charAt(i+1)))); 
    	return new String(baos.toByteArray()); 
    } 
    
    // 转化十六进制编码为字符串 
    public static String toStringHex(String s) 
    { 
    	byte[] baKeyword = new byte[s.length()/2]; 
    	for(int i = 0; i < baKeyword.length; i++) 
    	{ 
    		try 
    		{ 
    			baKeyword[i] = (byte)(0xff & Integer.parseInt(s.substring(i*2, i*2+2),16)); 
    		} 
    		catch(Exception e) 
    		{ 
    			e.printStackTrace(); 
    		} 
    	} 
    	try 
    	{ 
    		s = new String(baKeyword, "utf-8");//UTF-16le:Not 
    	} 
    	catch (Exception e1) 
    	{ 
    		e1.printStackTrace(); 
    	} 
    	return s; 
    } 
    
    //字符串替换
    public String replaceStr(String str, String regularExpression, String replacement){
    	String string = str.replaceAll(regularExpression, replacement);
		return string;
    }
    
    public String md5(String s) {  
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',  
                'a', 'b', 'c', 'd', 'e', 'f' };  
        try {  
            byte[] strTemp = s.getBytes();  
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");  
            mdTemp.update(strTemp);  
            byte[] md = mdTemp.digest();  
            int j = md.length;  
            char str[] = new char[j * 2];  
            int k = 0;  
            for (int i = 0; i < j; i++) {  
                byte byte0 = md[i];  
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];  
                str[k++] = hexDigits[byte0 & 0xf];  
            }  
            return new String(str);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }
    
    public boolean checkEmail(String email){
    	boolean tag = true;
        final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(email);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }
    
    public boolean checkPhone(String phone) {
    	String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
    	Pattern p = Pattern.compile(regExp);
    	Matcher m = p.matcher(phone);
    	return m.find();
    }
    
    public String getLocationTime() {
    	Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);
		return time;
	}
    
    public boolean isMobileNO(String mobiles){     
    	Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");     
    	Matcher m = p.matcher(mobiles);         
    	return m.matches();     
    } 
   
    public boolean isEmail(String email){     
    	String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
    	Pattern p = Pattern.compile(str);     
    	Matcher m = p.matcher(email);        
    	return m.matches();     
    }
    
}
