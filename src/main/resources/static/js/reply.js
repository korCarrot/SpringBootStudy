async function get1(bno){

    // axios.get : 비동기적으로 서버로부터 데이터를 요청합니다.
    // await : 해당 요청이 완료될 때까지 기다립니다.
    const result = await axios.get(`/replies/list/${bno}`)

    // console.log(result)

    return result;
}

// axios.get 메서드는 두 번째 매개변수로 설정 객체를 받을 수 있습니다. 이 설정 객체에 params 속성을 추가하면, 해당 속성에 지정된 객체가 URL 쿼리 매개변수로 변환됩니다.
// axios.get('/replies/list/123', { params: { page: 1, size: 10 } })를 호출하면 실제로 요청되는 URL은 /replies/list/123?page=1&size=10이 됩니다.

// async function getList({bno, page, size, goLast}){
//
//     const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}})
//
//     return result.data
// }


async function getList({bno, page, size, goLast}){

    const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}})

    if(goLast){
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total/size))

        return getList({bno:bno, page:lastPage, size:size})

    }

    return result.data
}

//댓글 등록
async function addReply(replyObj) {
    const response = await axios.post(`/replies/`,replyObj)
    return response.data
}

async function getReply(rno) {
    const response = await axios.get(`/replies/${rno}`)
    return response.data
}

async function modifyReply(replyObj) {

    const response = await axios.put(`/replies/${replyObj.rno}`, replyObj)
    return response.data
}

async function removeReply(rno) {
    const response = await axios.delete(`/replies/${rno}`)
    return response.data
}
