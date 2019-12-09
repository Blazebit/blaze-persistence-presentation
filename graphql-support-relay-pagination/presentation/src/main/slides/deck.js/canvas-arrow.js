(function(target) {
  if (!target || !target.prototype)
    return;
  target.prototype.arrow = function(startX, startY, endX, endY, controlPoints) {
    var dx = endX - startX;
    var dy = endY - startY;
    var len = Math.sqrt(dx * dx + dy * dy);
    var sin = dy / len;
    var cos = dx / len;
    var a = [];
    a.push(0, 0);
    for (var i = 0; i < controlPoints.length; i += 2) {
      var x = controlPoints[i];
      var y = controlPoints[i + 1];
      a.push(x < 0 ? len + x : x, y);
    }
    a.push(len, 0);
    for (var i = controlPoints.length; i > 0; i -= 2) {
      var x = controlPoints[i - 2];
      var y = controlPoints[i - 1];
      a.push(x < 0 ? len + x : x, -y);
    }
    a.push(0, 0);
    for (var i = 0; i < a.length; i += 2) {
      var x = a[i] * cos - a[i + 1] * sin + startX;
      var y = a[i] * sin + a[i + 1] * cos + startY;
      if (i === 0) this.moveTo(x, y);
      else this.lineTo(x, y);
    }
  };
})(CanvasRenderingContext2D);
(function ($) {
    $.each(['show', 'hide'], function (i, ev) {
        var el = $.fn[ev];
        $.fn[ev] = function () {
            this.trigger(ev);
            return el.apply(this, arguments);
        };
    });
})(jQuery);
function mapElementToElement(id, leftTxt, rightTxt) {
    (function ($) {
        var canvas = document.getElementById(id);
        $(canvas).ready(function () {
            var left = document.getElementsByClassName(id + "-1")[0];
            var right = document.getElementsByClassName(id + "-2")[0];
            var c = canvas.getContext("2d");
            c.lineWidth = 1;
            c.strokeStyle = "#1F93B8";
            c.fillStyle = "#1F93B8";
            var l = left.getElementsByClassName("pln")[0];
            var r = right.getElementsByClassName("pln")[0];
            var children = l.parentElement.children;
            for (var i = 0; i < children.length; i++) {
                if (children[i].innerHTML.indexOf(leftTxt) != -1) {
                    i++;
                    while (children[i].innerHTML.indexOf("\n") == -1) {
                        i++;
                    }
                    l = children[i - 1];
                    break;
                }
            }
            children = r.parentElement.children;
            for (var i = 0; i < children.length; i++) {
                if (children[i].innerHTML.indexOf(rightTxt) != -1) {
                    var idx;
                    while ((idx = children[i].innerHTML.indexOf("\n")) == -1) {
                       i--;
                    }
                    if (children[i].innerHTML.trim() == "") {
                        r = children[i + 1];
                    } else {
                        r = children[i];
                    }
                    break;
                }
            }
            var leftElem = l.getBoundingClientRect();
            var rightElem = r.getBoundingClientRect();
            var startX = leftElem.right;
            var startY = leftElem.top + (leftElem.bottom - leftElem.top) / 2;
            var endX = rightElem.left;
            var endY = rightElem.top + (rightElem.bottom - rightElem.top) / 2;
            c.beginPath();
            c.arrow(startX, startY, endX, endY, [0, 3, -20, 3, -20, 8]);
            c.fill();
        });
    })(jQuery);
}