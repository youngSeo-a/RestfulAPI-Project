package my.web.code.utils;

import org.springframework.stereotype.Component;

@Component
public class PagingVO {

	private int totalPage; // 전체 페이지 수
	private int nowPage; // 현재 페이지
	private int blockPerPageCount = Constants.BlOCK_PAGES; // 한블럭당 보여줄 페이지 수 
	private int pagePerRows; // 한페이지 당 보여줄 데이터 개수 
	private int totalRows; // 전체 데이터 개수 
	private int nowBlock; // 현재 블럭 위치
	private int totalBlock; // 전체 블럭 개수 
	
	//계산에 필요한 현재 페이지 위치와 전체 데이터 개수를 받는 메서드 
	public void setPageData(int nowPage, int totalRows) {
		this.nowPage = nowPage;
		this.totalRows = totalRows;
		this.pagePerRows = Constants.PAGE_ROWS;
	}
	
	public void setPageData(int nowPage, int totalRows, int pagePerRows) {
		this.nowPage = nowPage;
		this.totalRows = totalRows;
		this.pagePerRows = pagePerRows;
	}
	
	
	//sql에서 사용할 시작 조건
	public int getStart() {
		return this.nowPage * pagePerRows;
	}
	
	
	//sql에서 사용할 출력 개수
	public int getEnd() {
		return this.pagePerRows;
	}
	
	
	//전체 페이지수 계산 - 소수점도 남는페이지라서 올림처리 
	public int getTotalPage() {
		double val =  (double)this.totalRows / this.pagePerRows;
		this.totalPage = (int)(Math.ceil(val));
		return this.totalPage;
	}
	
	//현재 블럭 위치
	public int getNowBlock() {
		double val = (double)this.nowPage / this.blockPerPageCount;
		this.nowBlock  = (int)(Math.floor(val));
		return this.nowBlock;
	}
	
	//전체 블럭 개수
	public int getTotalBlock() {
		double val = (double)this.getTotalPage() / this.blockPerPageCount;
		this.totalBlock =  (int)(Math.ceil(val));
		return this.totalBlock ;
	}

	//html 제작!
	public String pageHTML() {
		 //html 코드를 넣을 빌더
		 StringBuilder sb = new StringBuilder();

		 //계산 메서드 호출 
		 this.getTotalPage();
		 this.getNowBlock();
		 this.getTotalBlock();
		 
		 //페이지 번호 변수
		 int pageNum = 0;
		 String isDisabled = "disabled";
		 //처음영역그리기
		  if(this.nowPage > 0) {
			  isDisabled= "";
		  }
			  sb.append("<li class=\"page-item" + isDisabled +"\">");
			  sb.append("<a class=\"page-link\" href=\"javascript:void(0);\" onclick=\"movePage(0);\">");
			  sb.append("First</a></li>");

		  
		  //이전으로 그리기
		  if(this.nowBlock > 0 ) {
			  pageNum = (this.nowBlock * this.blockPerPageCount) - 1;
			  sb.append("<li class=\"page-item\">");
			  sb.append("<a class=\"page-link\" href=\"javascript:void(0);\" onclick=\"movePage("+pageNum+");\">");
			  sb.append("Previous</a></li>"); 
		  }
		 
		  String isActive = "";
		  
		  for(int i = 0; i < this.blockPerPageCount; i++) {
			  isActive = "";
			  pageNum = (this.nowBlock * this.blockPerPageCount) + i;
			  
			  if(pageNum == this.nowPage) {
				  isActive = " active";
			  }
			  
			  sb.append("<li class=\"page-item"+ isActive +"\">");
			  sb.append("<a class=\"page-link\" href=\"javascript:void(0);\" onclick=\"movePage("+pageNum+");\">");
			  sb.append((pageNum+1) + "</a></li>"); 
			  
			  //페이지가 10개가 다 차지 않을 경우, 마지막 페이지에서 멈추도록 한다.
			  if(this.totalPage == (pageNum + 1)) {
				  break;
			  }
		  }
		  
		  //다음 블록 이동
		  if( (this.nowBlock +1)  < this.totalBlock ) {
			  pageNum = (this.nowBlock + 1) * this.blockPerPageCount;
			  sb.append("<li class=\"page-item\">");
			  sb.append("<a class=\"page-link\" href=\"javascript:void(0);\" onclick=\"movePage("+pageNum+");\">");
			  sb.append("Next</a></li>"); 
		  }

		  String lastEnabled = "disabled";
		  //마지막으로 가기 
		  if( (this.nowPage + 1)  < this.totalPage ) {
			  lastEnabled ="";
		  }
			  pageNum =  this.totalPage - 1;
			  sb.append("<li class=\"page-item"+ lastEnabled +"\">");
			  sb.append("<a class=\"page-link\" href=\"javascript:void(0);\" onclick=\"movePage("+pageNum+");\">");
			  sb.append("Last</a></li>"); 


		 return sb.toString();
		
	}
	
	
}
