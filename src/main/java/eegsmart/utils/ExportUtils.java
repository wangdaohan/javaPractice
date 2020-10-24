package eegsmart.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import eegsmart.Track;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.tika.metadata.Metadata;

public class ExportUtils {
	
	public static void exportToExcel(String[] titles, List<Track> tracks, String fileName) {
		
		//final Logger log = LoggerFactory.getLogger(ExportUtils.class);

		try {      
//			   //定义输出流，以便打开保存对话框______________________begin
//			   HttpServletResponse response=ServletActionContext.getResponse();
//			   OutputStream os = response.getOutputStream();// 取得输出流
//			   response.reset();// 清空输出流
//			   response.setHeader("Content-disposition", "attachment; filename="+ new String(fileName.getBytes("GB2312"),"ISO8859-1"));
//			// 设定输出文件头
//			   response.setContentType("application/msexcel");// 定义输出类型
			   
			   /** **********创建工作簿************ */
		     //  File excelFile = new File("");
			File excelFile = new File("D:/relax.csv");


			WritableWorkbook workbook = Workbook.createWorkbook(excelFile);

			   /** **********创建工作表************ */

			   WritableSheet sheet = workbook.createSheet("Sheet1", 0);

			   /** **********设置纵横打印（默认为纵打）、打印纸***************** */
			   jxl.SheetSettings sheetset = sheet.getSettings();
			   sheetset.setProtected(false);


			   /** ************设置单元格字体************** */
			   WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
			   WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10,WritableFont.BOLD);

			   /** ************以下设置三种单元格样式，灵活备用************ */
			   // 用于标题居中
			   WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
			   wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
			   wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			   wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
			   wcf_center.setWrap(false); // 文字是否换行
			   
			   // 用于正文居左
			   WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
			   wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			   wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			   wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
			   wcf_left.setWrap(false); // 文字是否换行   
			 

			   /** ***************以下是EXCEL开头大标题，暂时省略********************* */
			   //sheet.mergeCells(0, 0, colWidth, 0);
			   //sheet.addCell(new Label(0, 0, "XX报表", wcf_center));
			   /** ***************以下是EXCEL第一行列标题********************* */
			   for (int i = 0; i < titles.length; i++) {
			    sheet.addCell(new Label(i, 0,titles[i],wcf_center));
			   }   
			   /** ***************以下是EXCEL正文数据********************* */
			   Field[] fields=null;
			   
//			   for(Object obj:listContent){
//				   fields=obj.getClass().getDeclaredFields();
//				   int j=0;
//				   for(Field v:fields){
//					   v.setAccessible(true);
//					   Object va=v.get(obj);
//					   if(va==null){
//						   va="";
//					   }
//					   sheet.addCell(new Label(j, i,va.toString(),wcf_left));
//					   j++;
//				   }
//				   i++;
//			   }
			   //ti_id,account,status,name,nickname,sex,identification_card,school
			   int i=1;
			//需要的声音信息：
			//声音ID、声音名、时长、（最高品质）播放地址、声音标签、所属分类、所属分类标签、所属专辑
			//加上点赞数、播放数
			//再加上一个下载地址
			for(Track track:tracks){
				   int j=0;
				/*
				System.out.println("Title: " + metadata.get("title"));
            System.out.println("Artists: " + metadata.get("xmpDM:artist"));
            System.out.println("Composer : "+metadata.get("xmpDM:composer"));
            System.out.println("Genre : "+metadata.get("xmpDM:genre"));
            System.out.println("Album : "+metadata.get("xmpDM:album"));
				 */
				   sheet.addCell(new Label(j++,i++,track.getName(),wcf_center));
				   sheet.addCell(new Label(j++,i,track.getAlbum(),wcf_center));
				   sheet.addCell(new Label(j++,i,track.getDuration(),wcf_center));
				sheet.addCell(new Label(j++,i,track.getPlayURL(),wcf_center));
				sheet.addCell(new Label(j++,i,track.getSinger(),wcf_center));
			   }
			   /** **********将以上缓存中的内容写到EXCEL文件中******** */
			   workbook.write();
			   /** *********关闭文件************* */
			   workbook.close();
		}catch(Exception e){
			//log.error(e.getMessage());
			e.printStackTrace();
			//throw new Exception(e.getMessage(), e.getCause());
		}
	}

}
