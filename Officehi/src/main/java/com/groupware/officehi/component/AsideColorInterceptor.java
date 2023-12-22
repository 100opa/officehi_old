package com.groupware.officehi.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AsideColorInterceptor implements HandlerInterceptor {
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 현재 URL을 확인하고 모델에 추가
		log.info("preHandle 실행");
        String currentUrl = request.getRequestURI();
        log.info(currentUrl);
        request.setAttribute("currentUrl", currentUrl);
        return true;
    }	 
	
	@Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // 모델에 추가된 정보를 기반으로 헤더의 요소에 클래스를 추가
        if (modelAndView != null) {
            modelAndView.addObject("asideColorClass", determineAsideColorClass(request.getAttribute("currentUrl")));
            log.info("postHandle 실행");
        }
    }

    private String determineAsideColorClass(Object currentUrl) {
        // 현재 URL에 따라 적절한 클래스를 결정
        // 예: "/home"이라면 "home-header" 클래스 반환
        // 예: "/about"이라면 "about-header" 클래스 반환
        // 필요에 따라 복잡한 로직을 추가하여 클래스를 결정할 수 있음
        return currentUrl != null ? currentUrl.toString().replace("/", "") : "";
    }
}
