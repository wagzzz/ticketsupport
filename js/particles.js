/**
 * particles.js
 * Modifed from https://codepen.io/ns_bob/pen/BoMqqR
 *
 * @author Brice Purton
 * @version 1.0
 * @since 06-06-2018
 */


var canvas
var width;
var height;
var amount;
var particles;
var angle;
var style = "#5ec6ff";

function updateGlobals() {
	width = window.innerWidth;
	height = window.innerHeight;
	amount = parseInt(width/50);
	particles = [];
	for (var i = 0; i < amount; i++){
		particles.push({
			x: Math.random()*width,
			y: Math.random()*height,
			radius: Math.random()*50,
			density: Math.random()*amount
		});
	}
	angle = 0;
}

function createParticles() {
	updateGlobals();

	canvas = document.getElementById('particle_canvas');
	canvas.style.color = style;
	canvas.width = width;
	canvas.height = height;
	var context = canvas.getContext('2d');
	
	function drawParticles() {
		context.clearRect(0, 0, width, height);
		context.fillStyle = style;
		//context.fillStyle = 'rgba(50, 125, 218, 1)';
		context.beginPath();
		
		for (var i = 0; i < amount; i++) {
			var f = particles[i];
			context.moveTo(f.x, f.y);
			context.arc(f.x, f.y, f.radius, 0, Math.PI * 2, true);
		}
		context.fill()
		updateCanvas();
	}
	
	function updateCanvas() {
		angle += 0.01;
		for (var i = 0; i < amount; i++) {
			var f = particles[i];
			
			f.y += Math.cos(angle + f.density) + f.radius / 100;
			f.x += Math.sin(angle) * 2;
			
			if (f.x > width + 5 || f.x < -5 || f.y > height) {
				if (i % 3 > 0) {
					particles[i] = {x: Math.random()* width, y: -10, radius: f.radius, density: f.density};
				} else {
					if (Math.sin(angle) > 0) {
						particles[i] = {x: - 5, y: Math.random()*height, radius: f.radius, density: f.density};
					} else {
						particles[i] = {x: width + 5, y: Math.random()*height, radius: f.radius, density: f.density};
					}
				}
			}
		}
	}
	setInterval(drawParticles, 30);
}

window.onload = createParticles();

window.addEventListener("resize", function() {
	updateGlobals();
    canvas.width = width;
	canvas.height = height;
});