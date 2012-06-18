package jp.redmine.redmineclient.external;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import jp.redmine.redmineclient.entity.RedmineConnection;
import jp.redmine.redmineclient.entity.RedmineProject;
import jp.redmine.redmineclient.entity.TypeConverter;

public class ParserProject extends BaseParser<RedmineConnection,RedmineProject> {
	//private final String TAG = this.toString();

	@Override
	public void parse(RedmineConnection con) throws XmlPullParserException, IOException {
		super.parse(con);
		int eventType = xml.getEventType();
		RedmineProject item = null;
		Log.d("ParserProject","start parse");
		while (eventType != XmlPullParser.END_DOCUMENT) {
			eventType = xml.next();
			switch (eventType){
			case XmlPullParser.START_DOCUMENT:
				Log.d("ParserProject","START_DOCUMENT");
				break;
			case XmlPullParser.START_TAG:
				Log.d("ParserProject","START_TAG ".concat(xml.getName()));
				if("project".equalsIgnoreCase(xml.getName())){
					item = new RedmineProject();
				} else if(item != null){
					parseInternal(item);
				}
				break;
			case XmlPullParser.END_TAG:
				Log.d("ParserProject","END_TAG ".concat(xml.getName()));
				if("project".equalsIgnoreCase(xml.getName())){
					notifyDataCreation(con,item);
					item = null;
				}
				break;
			case XmlPullParser.TEXT:
				Log.d("ParserProject","TEXT ".concat(xml.getText()));
				break;
			}
		}

	}

	private void parseInternal(RedmineProject item) throws XmlPullParserException, IOException{
		if(xml.getDepth() <= 2)
			return;
		if("id".equalsIgnoreCase(xml.getName())){
			String work = getNextText();
			if("".equals(work))	return;
			item.setProjectId(Integer.parseInt(work));
		} else if("name".equalsIgnoreCase(xml.getName())){
			item.setName(getNextText());
		} else if("identifier".equalsIgnoreCase(xml.getName())){
			item.setIdentifier(getNextText());
		} else if("description".equalsIgnoreCase(xml.getName())){
			item.setDescription(getNextText());
		} else if("homepage".equalsIgnoreCase(xml.getName())){
			item.setHomepage(getNextText());
		} else if("created_on".equalsIgnoreCase(xml.getName())){
			item.setCreated(TypeConverter.parseDateTime(getNextText()));
		} else if("updated_on".equalsIgnoreCase(xml.getName())){
			item.setModified(TypeConverter.parseDateTime(getNextText()));
		}
		//@todo tracker, issue_categories

	}
}
