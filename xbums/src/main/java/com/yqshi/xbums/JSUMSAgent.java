/**
 * LYX Statics SDK FOR Android
 * <p>
 * An open source analytics android sdk for mobile applications
 *
 * @package LYX 利伊享
 * @author yqshi
 * @copyright Copyright (c) All Rights Reserved.
 * @since Version 1.0
 */
package com.yqshi.xbums;

import android.content.Context;
import android.webkit.JavascriptInterface;

public class JSUMSAgent {
	Context context;

	public JSUMSAgent(Context context) {
		super();
		this.context = context;
	}
	@JavascriptInterface 
	public void bindUserIdentifier(String identifier){
		UmsAgent.bindUserIdentifier(context, identifier);
	}
	@JavascriptInterface 
	public void onError(String errorType,String errorInfo){
		UmsAgent.onError(context, errorType,errorInfo);
		
	}
	@JavascriptInterface 
	public void postTags(String tags){
		UmsAgent.postTags(context, tags);
	}
	@JavascriptInterface 
	public void onEvent(String event_id){
		UmsAgent.onEvent(context, event_id);
	}
	@JavascriptInterface 
	public void onEvent(String event_id, int acc){
		UmsAgent.onEvent(context, event_id, acc);
	}
	@JavascriptInterface 
	public void onEvent(String event_id,
			 String label,  int acc) {
	    UmsAgent.onEvent(context, event_id, label, acc);
	}
	@JavascriptInterface 
	public void onEvent( String event_id,
			 String label,  String json) {
		UmsAgent.onEvent(context, event_id, label, json);
	}
	@JavascriptInterface 
	public void onGenericEvent(String label,String value){
		UmsAgent.onGenericEvent(context, label, value);
	}
	@JavascriptInterface
	public void postWebPage(String pageName){
		UmsAgent.postWebPage(pageName);
	}
}
