
(function (window) {
  window["env"] = window["env"] || {};

  // Environment variables
  window["env"]["environmentName"] = "${ENVIRONMENT_NAME}";
  window["env"]["apiBackEndUrl"] = "${API_BACKEND_URL}";
  window["env"]["debug"] = "${DEBUG}"
})(this);