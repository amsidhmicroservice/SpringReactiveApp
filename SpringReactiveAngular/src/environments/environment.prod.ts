
export const environment = {
  production: true,
  environmentName: (window as any)["env"]["environmentName"] || "Amsidh Prod Environment",
  apiBackEndUrl: (window as any)["env"]["apiBackEndUrl"] || "default",
  debug: (window as any)["env"]["debug"] || false
};
