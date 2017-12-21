window.onload=function(){
	let id =window.location.search.slice(1)
	// let id=1032
	$.ajax({
		type:"get",
		url:`/job/info/${id}`,
		dataType:"json",
		async:false,
		success: function (data) {
			if (data.status) {
				let res=data.data;
				$("#conz").html(
					`<div class="list-group-item">
					<h4 class="list-group-item-heading">${res.company}</h4>
					<div class="list-group-item-text row">
					<p class="col-xs-6">时间：${res.date}</p>
					<p class="col-xs-6">点击量：${res.click}</p>
					<p class="col-xs-6">地点：${res.place}</p>
					</div>
					</div>`
					)

				$("#con").html(res.info)
				console.log(data)
			}
		},
		error: function(){
			alert("请求出错");
		}
	});
	$("table").removeAttr("style")
	$("table").removeAttr("width")
	$("table").attr("class",'table table-striped table-bordered table-hover')
	$("table").wrap(`<div class="table-responsive"></div>`);
}