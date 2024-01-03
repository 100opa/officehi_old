package com.groupware.officehi.controller.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.groupware.officehi.controller.LoginController.SessionConst;
import com.groupware.officehi.domain.Paging;
import com.groupware.officehi.dto.LoginUserDTO;
import com.groupware.officehi.dto.NoticeDTO;
import com.groupware.officehi.dto.PagingDTO;
import com.groupware.officehi.service.NoticeService;

import lombok.RequiredArgsConstructor;
/**
 * @author 정유진
 * @editDate 23.12.19 ~ 23.12.20
 *  페이지네이션
 * @editDate 23.12.26 ~ 23.12.28
*/
	
@Controller
@RequiredArgsConstructor
@RequestMapping("/notices")
public class NoticeController {

	private final NoticeService noticeService;
	public LoginUserDTO loginUser = null;
	
	public boolean loginCheck(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(false);
		if (session == null)
			return true;

		this.loginUser = (LoginUserDTO) session.getAttribute(SessionConst.LOGIN_MEMBER);
		if (loginUser == null)
			return true;
		
		model.addAttribute("loginUser", loginUser);
		
		return false;
	}
	
	@GetMapping
	public String notices(Paging paging, Model model, HttpServletRequest request) {
		if(loginCheck(request, model))
			return "redirect:/login";
		
		int totalRow = noticeService.findAll().size();
		List<NoticeDTO> notices = noticeService.findAllPaging(paging);
		model.addAttribute("notices", notices);
		model.addAttribute("pageMaker", new PagingDTO(paging, totalRow));
		return "/user/notices/noticeList";
	}

	@GetMapping("/{noticeNo}")
	public String noticeDetail(@PathVariable("noticeNo") Long noticeNo, Model model, HttpServletRequest request) {
		if(loginCheck(request, model))
			return "redirect:/login";
		
		NoticeDTO notice = noticeService.findByNoticeNo(noticeNo).get();
		model.addAttribute("notice", notice);
		return "/user/notices/notice";
	}
	
	@GetMapping("/search")
	public String search(@RequestParam("search") String search, @RequestParam("searchValue") String searchValue, 
			@ModelAttribute("paging") Paging paging, Model model, HttpServletRequest request) {
		if(loginCheck(request, model))
			return "redirect:/login";
		
		List<NoticeDTO> notices;
		int totalRow = 0;
		
		switch(search) {
		case "title":
			totalRow = noticeService.findAllByTitle(searchValue).size();
			notices = noticeService.findAllByTitlePaging(searchValue, paging);
			break;
		case "content":
			totalRow = noticeService.findAllByContent(searchValue).size();
			notices = noticeService.findAllByContentPaging(searchValue, paging);
			break;
		default:
			return "/user/notices/noticeList";
		}
		
		model.addAttribute("notices", notices);
		model.addAttribute("pageMaker", new PagingDTO(paging, totalRow));
		return "/user/notices/noticeList";
	}
}