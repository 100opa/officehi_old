package com.groupware.officehi.controller.user;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.groupware.officehi.controller.LoginController.SessionConst;
import com.groupware.officehi.dto.ApprovalDTO;
import com.groupware.officehi.dto.EmployeeDTO;
import com.groupware.officehi.dto.FileDTO;
import com.groupware.officehi.dto.LoginUserDTO;
import com.groupware.officehi.dto.NoticeDTO;
import com.groupware.officehi.dto.WorkDTO;
import com.groupware.officehi.service.ApprovalService;
import com.groupware.officehi.service.EmployeeService;
import com.groupware.officehi.service.NoticeService;
import com.groupware.officehi.service.WorkService;

import lombok.RequiredArgsConstructor;

/**
 * @author 이승준
 * @editDate 23.12.20 ~ 23.12.21
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainController {

	private final NoticeService noticeService;
	private final ApprovalService approvalService;
	private final WorkService workService;
	private final EmployeeService employeeService;
	
	public LoginUserDTO loginUser = null;
	
	// 로그인 검증
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
	public String main(Model model, HttpServletRequest request) {
		if(loginCheck(request, model))
			return "redirect:/login";

		Optional<EmployeeDTO> user = employeeService.findUserInfoByUserNo(loginUser.getUserNo());
		model.addAttribute("user", user.get());

		Optional<FileDTO> profile = employeeService.findProfileFileByUserNo(loginUser.getUserNo());
		if (profile.isPresent())
			model.addAttribute("profile", profile.get());
		else
			model.addAttribute("profile", new FileDTO());

		Optional<NoticeDTO> notice = noticeService.findAll().stream().findFirst();
		if (notice.isPresent())
			model.addAttribute("notice", notice.get());

		List<ApprovalDTO> approvals = approvalService.findApprovalByUserNoOrChecker(loginUser.getUserNo(), null);
		model.addAttribute("approvals", approvals.stream().limit(7).collect(Collectors.toList()));
		
		return "/user/main";
	}

	@PostMapping("/arrival")
	public String arrival(RedirectAttributes redirectAttributes, Model model, HttpServletRequest request) {
		if(loginCheck(request, model))
			return "redirect:/login";

		Integer duplicateCheck = workService.checkDateDuplicte(loginUser.getUserNo());
		if (duplicateCheck != null) {
			redirectAttributes.addFlashAttribute("resultMessage", "이미 출근한 날짜입니다.");
			return "redirect:/main";
		} else {
			WorkDTO work = new WorkDTO();
			work.setUserNo(loginUser.getUserNo());
			workService.arrivalTimeCheck(work);
			redirectAttributes.addFlashAttribute("resultMessage", "출근 처리 되었습니다.");
			return "redirect:/main";
		}

	}

	@PostMapping("/leave")
	public String leave(Model model, HttpServletRequest request) {
		if(loginCheck(request, model))
			return "redirect:/login";

		WorkDTO work = new WorkDTO();
		work.setUserNo(loginUser.getUserNo());
		workService.leaveTimeCheck(work);
		return "redirect:/main";
	}

}
