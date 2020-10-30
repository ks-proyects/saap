var oWindow;
var opt;
// var pageName = 'index';
function AbrirPagina(Url, width, height) {
	oPagina = Url;
	try {
		opt = "dialogTop:" + (screen.availHeight - height) / 2
				+ "px;dialogLeft:" + (screen.availWidth - width) / 2
				+ "px;dialogWidth:" + width + "px;dialogHeight:" + height
				+ "px;center:yes;resizable:no;status:no;";
		oWindow = window.showModalDialog(oPagina, "Windows", opt);
		window.doPostBack();
	} catch (e) {
		opt = 'top='
				+ (screen.availHeight - height)
				/ 2
				+ ', left='
				+ (screen.availWidth - width)
				/ 2
				+ ', height='
				+ height
				+ ', width='
				+ width
				+ ', menubar=no, scrollbars=yes, status=no, toolbar=no ,directories=no,continued from previous linemenubar=no,scrollbars=no,resizable=no ,modal=yes';
		oWindow = window.open(oPagina, null, opt);
		oWindow.focus();
	} finally {
		var timer = null;
		timer = setInterval(function() {
			if (oWindow != undefined && oWindow.closed) {
				clearInterval(timer);
				doPostBack();
			}
		}, 1000);
	}
}

function closePopup() {
	oWindow.close();
}
function doPostBack() {
	document.getElementById("WorkPage:btn_3").click();
}
function onClicTE(idAction, aplicaMotivo, idCambioEstado, idDocumentoEntidad) {

	switch (idAction) {
	case "-4":
		switch (window.name) {
		case "planillaEdit":
			AbrirPagina('../pago/pagoEdit.jsf?idCambioEstado=' + idCambioEstado
					+ '&idDocumentoEntidad=' + idDocumentoEntidad, 450, 350);
			return false;
			break;
		case "periodoPagoEdit":
			showLoader();
			break;
		default:
			
			break;
		}
		break;
	default:
		if (confirm("\xbfEst\xe1 seguro que desea realizar esta acci\u00F3n?")) {
			switch (aplicaMotivo) {
			case "S":
				AbrirPagina('../data/cambioEstadoMotivo.jsf?idCambioEstado='
						+ idCambioEstado + '&idDocumentoEntidad='
						+ idDocumentoEntidad, 550, 420);
				return false;
				break;
			default:
				showLoader();
				return true;
				break;
			}
		} else {
			return false;
		}
		break;
	}

}
function showLoader() {
	document.getElementById('page_loader').style.display = "";
}

function load(page) {
	window.name = page;
	resize();
}
/**
 * 
 * @param page
 * @param idInputFirst
 */
function loader(page, idInputFirst) {
	window.name = page;
	document.getElementById(idInputFirst).focus();
	resize();
}

function muestraReloj() {
	var fechaHora = new Date();
	var horas = fechaHora.getHours();
	var minutos = fechaHora.getMinutes();
	var segundos = fechaHora.getSeconds();
	if (horas < 10) {
	}
	if (minutos < 10) {
		minutos = '0' + minutos;
	}
	if (segundos < 10) {
		segundos = '0' + segundos;
	}
	document.getElementById('HomeForm:divRelog').innerHTML = horas + ':'
			+ minutos + ':' + segundos;
}

function onClic(idAction) {

	switch (idAction) {
	case "1":
		switch (window.name) {
		case "usuarioList":
			showLoader();
			break;
		default:
			showLoader();
			break;
		}

	case "2":

		switch (window.name) {
		case "planillaEdit":
			showLoader();
			// AbrirPagina('../pago/pagoEdit.jsf', 450, 350);
			// return false;
			break;

		default:
			showLoader();
			break;
		}

		break;
	case "5":
		if (confirm("\xbfEst\xe1 seguro que desea Eliminar?")) {
			showLoader();
			return true;
		} else {
			return false;
		}
		// return confirm("\xbfEst\xe1 seguro que desea Eliminar?");
		break;
	case "11":
		switch (window.name) {
		case "creditoEdit":
			AbrirPagina('transaccionEdit.jsf?action=INS', 550, 420);
			return false;
			break;

		default:
			showLoader();
			break;
		}
		break;
	case "8":
		switch (window.name) {
		case "planillaEdit":
			if (confirm("\xbfEst\xe1 seguro que desea Imprimir?")) {
				return true;
			} else {
				return false;
			}
			break;

		default:
			showLoader();
			break;
		}
		break;
	case "12":
		return true;
	default:
		showLoader();
		break;
	}
}

function validarEmail(valor) {
	if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/.test(valor)) {
		return true;
	} else {
		return false;
	}
}

function ValidarNumerico(cadena, obj, e) {
	opc = false;
	tecla = (document.all) ? e.keyCode : e.which;
	var num = obj.value;
	if (tecla == 0 || tecla == 8 || tecla == 9)
		return true;

	if (cadena == "%d")
		if (tecla > 47 && tecla < 58)
			opc = true;

	if (cadena == "%f") {
		if (tecla > 47 && tecla < 58)
			return true;
		if (tecla == 46) {
			if (obj.value.length == 0)
				return false;
			if (num.indexOf('.') == -1)
				return true;
		}
		// if (obj.value.search("[.*]") == -1 && obj.value.length != 0)
		// if (tecla == 46) opc = true;
	}
	return opc;
}

function ValidarEntero(obj, e) {
	opc = false;
	tecla = (document.all) ? e.keyCode : e.which;

	// teclas de control
	if (tecla == 0 || tecla == 8)
		return true;

	if (tecla > 47 && tecla < 58)
		opc = true;
	return opc;
}

function ValidarLongitudTexto(longitud, obj, e) {
	tecla = (document.all) ? e.keyCode : e.which;
	if (tecla == 0 || tecla == 8)
		return true;
	if (obj.value.length > longitud) {
		alert('Este campo solo puede contener ' + longitud + ' caracteres.');
		return false;
	} else
		return true;
}

function ValidarLongitudNumerico(longitud, obj, e) {
	if (obj.value.length != longitud && obj.value.length != 0) {
		alert('Este campo debe tener ' + longitud + ' digitos.');
		obj.select();
		obj.focus();
	}
}
/**
 * M�todo invocado para la abreviaci�n mediante el teclado de los botones
 * 
 * @param e
 */
function onKeyUp(e) {
	if (e.shiftKey) {
		var tecla = (document.all) ? e.keyCode : e.which;
		switch (tecla) {
		case 66:
			if (document.getElementById("WorkPage:btn_1") != null)
				document.getElementById("WorkPage:btn_1").click();
		case 73:
			if (document.getElementById("WorkPage:btn_2") != null)
				document.getElementById("WorkPage:btn_2").click();
		case 82:
			if (document.getElementById("WorkPage:btn_3") != null)
				document.getElementById("WorkPage:btn_3").click();
			break;
		case 78:
			if (document.getElementById("WorkPage:btn_4") != null)
				document.getElementById("WorkPage:btn_4").click();
			break;
		case 69:
			if (document.getElementById("WorkPage:btn_5") != null)
				document.getElementById("WorkPage:btn_5").click();
			break;
		case 71:
			if (document.getElementById("WorkPage:btn_7") != null)
				document.getElementById("WorkPage:btn_7").click();
			break;
		case 80:
			if (document.getElementById("WorkPage:btn_8") != null)
				document.getElementById("WorkPage:btn_8").click();
			break;
		case 90:
			if (document.getElementById("WorkPage:btn_9") != null)
				document.getElementById("WorkPage:btn_9").click();
			break;
		case 88:
			if (document.getElementById("WorkPage:btn_10") != null)
				document.getElementById("WorkPage:btn_10").click();
			break;
		case 86:
			if (document.getElementById("WorkPage:btn_11") != null)
				document.getElementById("WorkPage:btn_11").click();
			break;
		// Cambio de estado

		case 81:
			if (document.getElementById("WorkPage:btn_-1") != null)
				document.getElementById("WorkPage:btn_-1").click();
			break;
		case 87:
			if (document.getElementById("WorkPage:btn_-2") != null)
				document.getElementById("WorkPage:btn_-2").click();
			break;
		case 84:
			if (document.getElementById("WorkPage:btn_-3") != null)
				document.getElementById("WorkPage:btn_-3").click();
			break;
		case 89:
			if (document.getElementById("WorkPage:btn_-4") != null)
				document.getElementById("WorkPage:btn_-4").click();
			break;
		case 85:
			if (document.getElementById("WorkPage:btn_-5") != null)
				document.getElementById("WorkPage:btn_-5").click();
			break;
		case 79:
			if (document.getElementById("WorkPage:btn_-6") != null)
				document.getElementById("WorkPage:btn_-6").click();
			break;
		case 65:
			if (document.getElementById("WorkPage:btn_-7") != null)
				document.getElementById("WorkPage:btn_-7").click();
			break;
		default:
			break;
		}
	}
}