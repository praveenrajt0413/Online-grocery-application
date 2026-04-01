The error you're seeing in Jenkins—"couldn't get current server API group list: the server could not find the requested resource"—means that the kubectl tool within the Jenkins environment cannot connect to a running Kubernetes cluster. By default, it's trying to connect to a local cluster at localhost:8080 and failing.

Since this is running on your lab system, we need to figure out the current state of Kubernetes on this machine. Here are the two most likely scenarios:

1. You don't have a Kubernetes cluster running
If you haven't started a cluster like Minikube or enabled Kubernetes in Docker Desktop, kubectl has nothing to deploy to.

How to verify: Open your command prompt (outside of Jenkins) and run:

bash
kubectl get nodes
If this command fails for you as well, you need to either start a Kubernetes cluster (e.g., install and start Minikube with minikube start) or, if this pipeline is only meant to test Docker pushing, remove the 'Deploy to Kubernetes' stage from your Jenkinsfile.

2. You DO have a cluster running, but Jenkins can't see it
If you do have a local Kubernetes cluster and kubectl get nodes works perfectly fine in your normal command prompt, the issue is that Jenkins is running as a Windows Service.

Windows Services run under a different user account (usually "Local System" or a dedicated Jenkins user), meaning Jenkins doesn't have access to your personal kubeconfig file located at C:\Users\Praveen Raj\.kube\config.

How to fix this: You need to copy your .kube configuration to the Jenkins user profile:

Open your File Explorer.
Copy the config file located at C:\Users\Praveen Raj\.kube\config.
Paste it into the Jenkins user's home directory. If Jenkins is running as the Local System account, this is typically C:\Windows\System32\config\systemprofile\.kube\config. You will likely need to create the .kube folder there first.
Alternatively, you could provide the Kubeconfig file to Jenkins using the Kubernetes CLI Plugin in Jenkins and add it as a credential, then wrap your pipeline step like this:

groovy
stage('Deploy to Kubernetes') {
    steps {
        withKubeConfig([credentialsId: 'my-kubeconfig-credential']) {
            bat 'kubectl apply -f deployment.yaml'
        }
    }
}
Let me know if you have a cluster running on this machine or if you want to set one up, and I can guide you through the next steps!

