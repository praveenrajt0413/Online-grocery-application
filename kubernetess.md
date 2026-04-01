Here is a complete, step-by-step guide you can follow on your lab system to resolve this issue and successfully deploy to Kubernetes from Jenkins.

The error happens because Jenkins on Windows runs as a hidden background system account (usually "Local System"). This background account doesn't know where your Kubernetes cluster is because it doesn't have access to your regular user's configuration file (`.kube/config`).

Here is how to check your cluster status and connect it to Jenkins.

### Step 1: Verify Kubernetes is Running on the Lab System
Before connecting Jenkins, make sure a Kubernetes cluster (like Docker Desktop or Minikube) is actually running.

1. Open a regular Command Prompt or PowerShell on the lab computer.
2. Run the following command:
   ```cmd
   kubectl get nodes
   ```
3. **If you see an error** (like *connection refused* or *the server could not find the requested resource*): You do not have a cluster running. You need to either start Minikube (`minikube start`) or enable Kubernetes inside Docker Desktop's settings and wait for it to start.
4. **If you see a successful output** (listing the nodes like `minikube   Ready` or `docker-desktop   Ready`): Your cluster is working perfectly. Proceed to Step 2.

---

### Step 2: Connect Jenkins to Kubernetes
You can use one of two methods to give Jenkins access. **Method A** is the best practice for CI/CD pipelines. **Method B** is a quick file-copy hack that works well for local labs.

#### Method A: The Proper Way (Using Jenkins Credentials)
This method ensures your pipeline explicitly loads the Kubernetes configuration.

1. **Locate your `kubeconfig` file:** Go to your user directory (e.g., `C:\Users\YourLabUser\.kube\`) and find the file simply named `config`. Note where this is.
2. **Install the Kubernetes Plugin in Jenkins:**
   * Open Jenkins in your browser.
   * Go to **Manage Jenkins** -> **Plugins** -> **Available plugins**.
   * Search for **Kubernetes CLI Plugin** and install it (restart Jenkins if needed).
3. **Add the config file to Jenkins Credentials:**
   * Go to **Manage Jenkins** -> **Credentials** -> **System** -> **Global credentials (unrestricted)**.
   * Click **Add Credentials**.
   * Pick **Kind:** `Secret file`.
   * Click **Choose File** and upload the `config` file you found in step 1.
   * Set the **ID** to: `kubeconfig-lab`.
   * Click **Create**.
4. **Update your `Jenkinsfile`:**
   Change your Deploy stage to safely load this configuration before running the `kubectl apply` command. Update your `Jenkinsfile` to look like this:
   ```groovy
   stage('Deploy to Kubernetes') {
       steps {
           // We use withKubeConfig to temporarily provide the credential to kubectl
           withKubeConfig([credentialsId: 'kubeconfig-lab']) {
               bat 'kubectl apply -f deployment.yaml'
           }
       }
   }
   ```

#### Method B: The Quick Hack (Copying the Config File)
Jenkins usually runs under the hidden Windows `SYSTEM` account. You can just copy your working config into the `SYSTEM` account's home folder.

1. Make sure `kubectl get nodes` works on your normal command prompt.
2. Open File Explorer as an **Administrator**.
3. Browse to your personal Kubernetes folder: `C:\Users\YourLabUser\.kube\` and copy the `config` file.
4. Now, navigate to the Jenkins "Local System" home directory:
   `C:\Windows\System32\config\systemprofile\`
5. Create a new folder there named `.kube` (so you have `C:\Windows\System32\config\systemprofile\.kube`).
6. Paste the `config` file into this new `.kube` folder.
7. Open Windows **Services** (Press Windows Key, type `services.msc`). Find the **Jenkins** service, right-click it, and pick **Restart**.
8. Go back to Jenkins and re-run your build! It should now magically find the cluster.

I highly recommend **Method A** as you will learn how CI/CD pipelines securely handle deployment credentials in the real world. Let me know if you hit any snags while executing this in the lab!
