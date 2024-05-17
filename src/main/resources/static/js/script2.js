/* text_iife.js */
// 텍스트 작성과 삭제 즉시 실행 함수
(function(){
    const spanEl = document.querySelector("main h2 span");
    const txtArr = ['Back-End Developer', 'Web-Developer', 'Java-Developer']; //span에 들어갈 문자열 = 메인에 나타날 문자열
    let index=0;
    let currentTxt = txtArr[index].split(""); // txtArr[인덱스 순서]의 문자열을 하나씩 분할해서 저장
    function writeTxt(){    // *(앞에서) 한 글자씩 나타나게 하는 함수
        spanEl.textContent += currentTxt.shift(); //currentTxt 배열에서 첫 글자를 제거하고 spanEl의 텍스트 내용에 추가합니다.  //shift() : 배열에서 첫 번째 요소를 제거하고, 제거된 요소를 반환합니다. 이 메서드는 배열의 길이를 변하게 합니다. / 제거된 글자들을 저장하는 모습
        if(currentTxt.length !==0){
            setTimeout(writeTxt, Math.floor(Math.random() * 100)); //Math.floor() : 반올림해서 작은 수 선택 (ex. 5.7 -> 5)
        }else{
            currentTxt = spanEl.textContent.split("");  //spanEl.textContent에 들어간 글자들을 다시 분할
            setTimeout(deleteTxt, 3000);    //3초 뒤에 deleteTxt() 실행
        }
    }
    function deleteTxt(){   // *(뒤에서) 한 글자씩 사라지게 하는 함수
        currentTxt.pop();   //pop() : 배열에서 마지막 요소를 제거하고 그 요소를 반환합니다. 여기서는 반환 값을 받고 있진 않음(함수가 끝나고 다른 문자열도 나타내기 위해서).
        spanEl.textContent = currentTxt.join(""); //(맨 뒷 글자를 삭제한 이후) join으로 합쳐서 문자열 반환 -> spanEl태그에 문자열 나타나게 함 //Join() : 배열의 모든 요소를 쉼표나 지정된 구분 문자열로 구분하여 연결한 새 문자열을 만들어 반환
        if(currentTxt.length !== 0){
            setTimeout(deleteTxt, Math.floor(Math.random() * 100))
        }else{  //글자가 모두 지워진 상태
            index = (index + 1) % txtArr.length; //인덱스 순서 변경
            currentTxt = txtArr[index].split(""); //다음 txtArr의 문자열 저장
            writeTxt();
        }
    }   //txtArr의 모든 문자열의 작성과 삭제가 끝난 경우
    writeTxt(); //처음으로 writeTxt() 실행
})();   //무한 반복
/* end text_iife.js */

/* scroll_request.js */
/* 수직 스크롤이 발생하면 header 태그에 active 클래스 추가 및 삭제 */
//상단 메뉴들이 스크롤했을 때에도 유지됨.
const headerEl = document.querySelector("header");
window.addEventListener('scroll', function(){ //사용자가 페이지를 스크롤할 때마다 scroll 이벤트가 발생하며, 콜백 함수가 실행
    requestAnimationFrame(scrollCheck); //requestAnimationFrame은 성능을 최적화하기 위해 사용 //scrollCheck 함수를 다음 가능한 화면 리프레시 시점에 호출하도록 요청합니다
});
function scrollCheck(){
    let browerScrollY = window.scrollY ? window.scrollY : window.pageYOffset; //현재 페이지의 스크롤 위치를 확인
    if(browerScrollY > 0){                //classList 객체는 Element의 클래스 속성의 컬렉션을 나타내며, 클래스 추가, 제거, 토글 등을 쉽게 할 수 있는 메서드를 제공
        headerEl.classList.add("active"); //add(className): 지정된 클래스(className)를 요소의 클래스 목록에 추가
    }else{                                //index.html의 header에 "active" 클래스를 추가.
        headerEl.classList.remove("active"); //header.active는 CSS에서 정의되어 있음.
    }
}
/* end scroll_request.js */

/* move.js */
/* 애니메이션 스크롤 이동 */
const animationMove = function(selector){   //animationMove()는 scrollMoveEl에서 사용된다.
    // ① selector 매개변수로 이동할 대상 요소 노드 가져오기
    const targetEl = document.querySelector(selector);

    // ② 현재 브라우저의 스크롤 정보(y 값)
    const browserScrollY = window.pageYOffset;

    // ③ 이동할 대상의 위치(y 값)    (이동할 타겟 : targetEl)
    const targetScorllY = targetEl.getBoundingClientRect().top + browserScrollY;
    // ④ 스크롤 이동
    window.scrollTo({ top: targetScorllY, behavior: 'smooth' });
};

// 스크롤 이벤트 연결하기
const scollMoveEl = document.querySelectorAll("[data-animation-scroll='true']"); //header에 data-animation-scroll="true"인 버튼들 있음
for(let i = 0; i < scollMoveEl.length; i++){
    scollMoveEl[i].addEventListener('click', function(e){
        // const target = 이동하고자 하는 요소의 id값
        const target = this.dataset.target;  //dataset은 HTML 요소의 data-* 속성들을 접근할 수 있게 해주는 JavaScript 객체. 모든 data-* 속성은 dataset 객체의 프로퍼티로 변환.
        animationMove(target); //this.dataset은 클릭된 요소의 모든 data-* 속성을 포함하는 객체. this.dataset.target은 data-target 속성의 값을 가져옵니다.
    }); //결론적으로 index.html에 있는 data-target으로 사용자 정의한 속성의 값을 가져온다는 것.
}
/* End move.js */

//data-* 속성은 HTML5에서 도입된 표준으로, 사용자 정의 데이터를 요소에 저장할 수 있습니다.
// data-로 시작하는 모든 속성은 유효한 HTML입니다.
// 예를 들어, <div data-example="value"></div>에서 data-example은 사용자 정의 데이터 속성입니다.

