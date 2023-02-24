package study.datajpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.datajpa.entity.Member;

@Data
@AllArgsConstructor
public class MemberDto {

	private Long id;
	private String username;
	private String teamName;

	public MemberDto(Member member) {
		id = member.getId();
		username = member.getUsername();
	}
}
