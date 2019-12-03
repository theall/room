package lan.utils;

public class CmdParser {

	public static class Cmd {
		public int index;
		public String data;
	}
	public static Cmd parse(String s) {
		Cmd cmd = new CmdParser.Cmd();
		int index = s.indexOf(' ');
		if(index == -1) {
			cmd.index = Integer.parseInt(s);
		} else {
			String s1 = s.substring(0, index);
			String s2 = s.substring(index+1, s.length());
			cmd.index = Integer.parseInt(s1);;
			cmd.data = s2;
		}
		return cmd;
	}
}
