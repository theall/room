package lan.utils;

public class CmdParser {//cmd ������

	public static class Cmd {//����һ��Cmd����
		public int index;//����ı��
		public String data;//���������
	}
	public static Cmd parse(String s) {
		Cmd cmd = new CmdParser.Cmd();//������һ������ΪCmdParser.Cmd��������
		int index = s.indexOf(' ');//���ո��������������ھͽ���������ֵ�������ھͷ���-1
		if(index == -1) {//�����ⲻ���ո�
			cmd.index = Integer.parseInt(s);//��sת��Ϊint����
		} else {//�������
			String s1 = s.substring(0, index);//��ȡ�±�0���ո��±���ַ���
			String s2 = s.substring(index+1, s.length());//��ȡ�ո��±�+1��s�ַ������ȵ��ַ���
			cmd.index = Integer.parseInt(s1);//��s1ת�����Σ�������index��
			cmd.data = s2;//��s2����data��
		}
		return cmd;//��cmd���س�ȥ
	}
}
