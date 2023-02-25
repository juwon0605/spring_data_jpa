package study.datajpa.repository;

//interface는 프록시 객체로 반환하지만, 구체 클래스는 구체 클래스 자체로 반환한다
public class UsernameOnlyDto {

	private final String username;

	//생성자의 파라미터명을 기준으로 쿼리를 조회한다
	public UsernameOnlyDto(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
}
