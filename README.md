# gcp-report

# Project Setup

Intellij Setup:  
You need to generate a Credential Json file for your google cloud account. To do that follow the steps provided in the below link.   
https://cloud.google.com/docs/authentication/production#auth-cloud-implicit-java
 
Setup the environment variable for the project.    
``Edit Configuration -> configuration -> Environment -> Environment Variable``   

Add the below environment variable   
Name : `GOOGLE_APPLICATION_CREDENTIALS`  
Value : `Json Credentilal file path`

#Credential JSON file format
```
{
  "type": "service_account",
  "project_id": "magnartes",
  "private_key_id": "f04c487a75b***********6483dbbe6ad5b",
  "private_key": "-----BEGIN PRIVATE KEY-----\nMIIEvgIBADANBgkqhk8**************AoIBAQDC683DNmqWwxbR\nb3*********rjbGBAIwQ\n5zk7di5g51gIWX9WXNatw1WyPw3z4fuoIO+\n-----END PRIVATE KEY-----\n",
  "client_email": "gcp-catalog@magnartes.iam.gserviceaccount.com",
  "client_id": "11127131******6513029",
  "auth_uri": "https://accounts.google.com/o/oauth2/auth",
  "token_uri": "https://oauth2.googleapis.com/token",
  "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
  "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/gcp-catalog%40magnartes.iam.gserviceaccount.com"
}
```

