package lan.utils;

public class CmdParser {//cmd 解析器

	public static class Cmd {//创建一个Cmd方法
		public int index;//命令的编号
		public String data;//命令的内容
	}
	public static Cmd parse(String s) {
		Cmd cmd = new CmdParser.Cmd();//创建了一个对象为CmdParser.Cmd数据类型
		int index = s.indexOf(' ');//检查空格的索引，如果存在就将返回索引值，不存在就返回-1
		if(index == -1) {//如果监测不到空格
			cmd.index = Integer.parseInt(s);//将s转换为int整形
		} else {//如果存在
			String s1 = s.substring(0, index);//截取下标0到空格下标的字符串
			String s2 = s.substring(index+1, s.length());//截取空格下标+1到s字符串长度的字符串
			cmd.index = Integer.parseInt(s1);//将s1转成整形，并给到index里
			cmd.data = s2;//将s2给到data里
		}
		return cmd;//将cmd返回出去
	}
}
