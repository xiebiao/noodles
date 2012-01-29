package com.xiebiao.web.renderer;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xiebiao.web.MediaType;
import com.xiebiao.web.RequestContext;
import com.xiebiao.web.exception.RenderException;

/**
 * 
 * @author xiaog (joyrap@gmail.com)
 * 
 */
public class TextRenderer implements Renderer {
	private MediaType _mediaType;
	private String _data;

	public TextRenderer(MediaType mediaType, String data) {
		this._mediaType = mediaType;
		this._data = data;
	}

	public void render(HttpServletRequest request, HttpServletResponse response)
			throws RenderException {
		try {
			response.setContentType(this._mediaType.getName());
			response.setCharacterEncoding(RequestContext.ENCODING);
			Writer wr = response.getWriter();
			wr.write(_data);
			wr.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RenderException("");
		}
	}

}
