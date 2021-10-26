## SpringReactiveApp

### This application has following projects 
  - Spring boot reactive with Mongo Database
  - Angular with EventSource for reactive(non-blocking calls) backend call. 
    Simply display reactive data in html table

## Local Setup
  - Checkout the project
    ### SpringReactiveWebApp Setup
     - Build SpringReactiveWebApp with following command
        maven clean install
     - Build the docker using following command.
       docker build -t amsidhmicroservice/springreactivewebapp . --force-rm
     - Push the image to docker hub repository
       docker push amsidhmicroservice/springreactivewebapp:latest
     - 
    ### SpringReactiveAngular
     - Build the project with docker command
       docker build -t amsidhmicroservice/springreactiveangular . --force-rm
     - push the image to docker hub repository
       docker push amsidhmicroservice/springreactiveangular
   

## K8S Setup
   - Mongo Database installation on k8s
     - Run deployement script present under /deployment/1-mongo-db
           kubectl apply -f .\deployment\1-mongo-db\
   - SpringBoot Reactive App installation on k8s
     - Run deployement script present under /deployment/2-spring-backend
          kubectl apply -f .\deployment\2-spring-backend\
   - Angular Front End App installation on k8s
     - First update the property API_BACKEND_URL in 3-angular-frontend\angular-frontend-deployment.yaml
       with LoadBalancer/EXTERNAL-IP of Spring Backend Service (spring-backend-service)
        -- This is done because angular won't resove the host name using service-name in k8s.
     - Run deployement script present under /deployment/3-angular-frontend
          kubectl apply -f .\deployment\3-angular-frontend\ 

## Gateway, Virtual Service and ClusterRoleBinding Setup
   - For Gateway and Virtual Service run the following command
        kubectl apply -f .\deployment\5-gateway\
   - For ClusterRoleBinding
       kubectl apply -f .\deployment\4-rbac\          

## ISTIO Setup in GKE
   - For installing istio in GKE following the instruction provided in following url.
        https://istio.io/latest/docs/setup/getting-started/
   - Check installation is successfully or not using following command.
       kubectl get all -n istio-system
       It will show service/istio-ingressgateway external ip

## END Point Exposed by this Application
   - http://{istio-ingressgateway_EXTERNAL-IP}/
   - http://{istio-ingressgateway_EXTERNAL-IP}/persons/stream1   
   - 
    
