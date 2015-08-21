/*
;(function($, window, undefined){
	
	"use strict";
	
	$(document).ready(function()
	{
		if($('.page-loading-overlay').length)
		{
			$(window).on('load', function()
			{
				setTimeout(setupWidgets, 200);
			});
		}
		else
		{
			setupWidgets();
		}	
	});
	
	
	var setupWidgets = function()
	{		
		// Todo List
		$(".xe-todo-list").on('change', 'input[type="checkbox"]', function(ev)
		{
			var $cb = $(this),
				$li = $cb.closest('li');
			
			$li.removeClass('done');
				
			if($cb.is(':checked'))
			{
				$li.addClass('done');
			}
		});
		
		
		$(".xe-status-update").each(function(i, el)
		{
			var $el          	= $(el),
				$nav            = $el.find('.xe-nav a'),
				$status_list    = $el.find('.xe-body li'),
				index           = $status_list.filter('.active').index(),
				auto_switch     = attrDefault($el, 'auto-switch', 0),
				as_interval		= 0;
				
			if(auto_switch > 0)
			{
				as_interval = setInterval(function()
				{
					goTo(1);
					
				}, auto_switch * 1000);
				
				$el.hover(function()
				{
					window.clearInterval(as_interval);
				},
				function()
				{
					as_interval = setInterval(function()
					{
						goTo(1);
						
					}, auto_switch * 1000);;
				});
			}
			
			function goTo(plus_one)
			{
				index = (index + plus_one) % $status_list.length;
				
				if(index < 0)
					index = $status_list.length - 1;
				
				var $to_hide = $status_list.filter('.active'),
					$to_show = $status_list.eq(index);
				
				$to_hide.removeClass('active');
				$to_show.addClass('active').fadeTo(0,0).fadeTo(320,1);
			}
			
			$nav.on('click', function(ev)
			{
				ev.preventDefault();
				
				var plus_one = $(this).hasClass('xe-prev') ? -1 : 1;
				
				goTo(plus_one);
			});
		});
	}
	

})(jQuery, window);
*/