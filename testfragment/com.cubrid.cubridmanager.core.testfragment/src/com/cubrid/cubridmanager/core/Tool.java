package com.cubrid.cubridmanager.core;

import java.io.BufferedReader;
import java.io.FileReader;

import com.cubrid.common.core.util.CipherUtils;
import com.cubrid.common.core.util.CompatibleUtil;
import com.cubrid.cubridmanager.core.common.model.ServerInfo;

public class Tool {
	/**
	 * Return String of the content of a file
	 * 
	 * @param file String filepath and the filename
	 * @return
	 * @throws Exception
	 */
	public static String getFileContent(String file) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(file));
		StringBuffer bf = new StringBuffer();
		String line = null;
		while (null != (line = in.readLine())) {
			bf.append(line).append("\n");
		}
		in.close();
		return bf.toString();
	}

	public static String decryptContent(ServerInfo serverInfo, String content) {
		String[] lines = content.trim().split("\n");
		StringBuffer strBuf = new StringBuffer();
		for (String line : lines) {
			if (CompatibleUtil.isSupportCipher(serverInfo.getServerVersionKey())) {
				if (line.startsWith("@")) {
					line = line.substring(1);
					int pos = line.indexOf(":");
					String key = line.substring(0, pos);
					String value = line.substring(pos + 1);
					value = CipherUtils.decrypt(value);
					strBuf.append(key).append(":").append(value).append("\n");
				} else {
					strBuf.append(line).append("\n");
				}
			} else {
				strBuf.append(line).append("\n");
			}
		}
		strBuf.append("\n");
		return strBuf.toString();
	}

	public static void main(String[] args) throws Exception {
		// URL url = AllTest.class.getResource("/");
		// url = new
		// URL(url,"../src/com/cubrid/cubridmanager/core/test/messageSendReceiveAdminTest.txt");
		//		
		//		
		// FileInputStream fin = new FileInputStream(url.getFile());
		// FileChannel fc = fin.getChannel();
		// WritableByteChannel out = Channels.newChannel(System.out);
		// ByteBuffer buffer = ByteBuffer.allocate(102);
		// fc.read(buffer);
		// buffer.flip();
		//
		// out.write(buffer);
		//
		// buffer.clear();
		// fc.read(buffer);
		// buffer.flip();
		// out.write(buffer);
		// Compile regular expression

		//		Pattern pattern = Pattern.compile("^(.*)", Pattern.MULTILINE);
		//		Matcher matcher = pattern.matcher("1\n2\n3\n");
		//		String tabstring = matcher.replaceAll("\t$1");

	}
}
