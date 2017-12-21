window.onload=function(){
	let page =window.location.search.slice(1)
	// let page=1;
	$("#currentPage").html(page);
	if(getCookie('haveOpen')==1){
		getAllData(page)
	}else if(getCookie('haveOpen')==0){
		getOpenData(page)
	}else{
		setCookie('haveOpen',1,1)
	}
	$("#down").click(()=>{
		window.location.href=`/index?1`
		let haveOpen=getCookie('haveOpen')
		if (haveOpen==1) {
			getOpenData(1)
			setCookie('haveOpen',0,1)
		}else{
			getAllData(1)
			setCookie('haveOpen',1,1)
		}
		
	});
	$("#upOne").click(function(){
		if (parseInt(page)>1) {
			window.location.href=`/index?${parseInt(page)-1}`
			if (getCookie('haveOpen')==1) {
				getAllData(parseInt(page)-1)
			}else{
				getOpenData(parseInt(page)-1)
			}


		}
	})
	$("#downOne").click(function(){
		if (parseInt(page)<parseInt($("#allPage").html())) {
			window.location.href=`/index?${parseInt(page)+1}`
			if (getCookie('haveOpen')==1) {
				getAllData(parseInt(page)+1)
			}else{
				getOpenData(parseInt(page)+1)
			}

		}
	})
}

function getAllData(index){
	$.ajax({
		type:"get",
		url:`/job/${index}`,
		dataType:"json",
		async:false,
		success: function (data) {
			if(data.status){
				$("#allPage").html(data.data.pages)
				$("#currentPage").html(data.data.pageNum)
				let res=data.data.list;
				let html=""
				for(let i of res){
					if (i.company.length>6) {
						i.company=i.company.slice(0,6)+'...'
					}
					if (i.place.length>5) {
						i.place=i.place.slice(0,5)+'...'
					}
					if(!i.expired){
						html+=`<a href="/content?${i.id}" class="list-group-item row">
						<h4 class="list-group-item-heading col-xs-6">${i.company}<span style="font-size:5px;color:#6f1e1f">（未开）</span></h4>
						<p class="list-body line col-xs-3 ">${i.place}</p>
						<p class="list-body col-xs-3">${i.date}</p>
						</a>`
					}else {
						html+=`<a href="/content?${i.id}" class="list-group-item row">
						<h4 class="list-group-item-heading col-xs-6">${i.company}<span style="font-size:5px">（结束）</span></h4>
						<p class="list-body line col-xs-3 ">${i.place}</p>
						<p class="list-body col-xs-3">${i.date}</p>
						</a>`
					}

				}
				$("#allData").html(html)
				console.log(data)
			}
		},
		error: function(){
			alert("请求出错");
		}
	});
}

function getOpenData(index){
	$.ajax({
		type:"get",
		url:`/job/enable/${index}`,
		dataType:"json",
		async:false,
		success: function (data) {
			if(data.status){
				$("#allPage").html(data.data.pages)
				$("#currentPage").html(data.data.pageNum)
				let res=data.data.list;
				let html=""
				for(let i of res){
					if (i.company.length>6) {
						i.company=i.company.slice(0,6)+'...'
					}
					if (i.place.length>5) {
						i.place=i.place.slice(0,5)+'...'
					}
					html+=`<a href="/content?${i.id}" class="list-group-item row">
					<h4 class="list-group-item-heading col-xs-6">${i.company}<span style="font-size:5px;color:#6f1e1f">（未开）</span></h4>
					<p class="list-body line col-xs-3 ">${i.place}</p>
					<p class="list-body col-xs-3">${i.date}</p>
					</a>`
				}
				$("#allData").html(html)
				console.log(data)
			}
		},
		error: function(){
			alert("请求出错");
		}
	});

}

function getDate(strDate) { 
	var st = strDate; 
	var a = st.split(" "); 
	var b = a[0].split("-"); 
	var c = a[1].split(":"); 
	var date = new Date(b[0], b[1], b[2], c[0], c[1], c[2]);
	return date; 
} 


function setCookie (cname, cvalue, exdays) {
	var d = new Date();
	d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
	var expires = "expires=" + d.toUTCString();
	document.cookie = cname + "=" + cvalue + "; " + expires;
}

function getCookie (cname) {
	var name = cname + "=";
	var ca = document.cookie.split(';');
	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ') c = c.substring(1);
		if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
	}
	return "";
}