provider "scaleway" {
  zone            = "fr-par-1"
  region          = "fr-par"
}

resource "scaleway_instance_security_group" "my-security-group" {
  inbound_default_policy  = "drop"
  outbound_default_policy = "accept"

  inbound_rule {
    action = "accept"
    port   = "22"
  }

  inbound_rule {
    action = "accept"
    port   = "80"
  }

  inbound_rule {
    action = "accept"
    port   = "443"
  }
}

resource "scaleway_object_bucket" "frontend" {
  name = "frontend"
  acl  = "public-read"
  tags = {
    key = "value"
  }
}

resource "scaleway_registry_namespace" "main" {
  name        = "main_container_registry"
  description = "Main container registry"
  is_public   = false
}

resource "scaleway_k8s_cluster" "main" {
  name             = "main"
  description      = "The main cluster"
  version          = "1.20.5"
  cni              = "calico"
  tags             = ["i'm an awsome tag"]

  autoscaler_config {
    disable_scale_down              = false
    scale_down_delay_after_add      = "5m"
    estimator                       = "binpacking"
    expander                        = "random"
    ignore_daemonsets_utilization   = true
    balance_similar_node_groups     = true
    expendable_pods_priority_cutoff = -5
  }
}

resource "scaleway_k8s_pool" "api" {
  cluster_id  = scaleway_k8s_cluster.main.id
  name        = "api"
  node_type   = "DEV1-M"
  size        = 1
  autoscaling = true
  autohealing = true
  min_size    = 1
  max_size    = 5
}

resource "scaleway_k8s_pool" "auth" {
  cluster_id  = scaleway_k8s_cluster.main.id
  name        = "auth"
  node_type   = "DEV1-M"
  size        = 1
  autoscaling = true
  autohealing = true
  min_size    = 1
  max_size    = 5
}

resource "null_resource" "kubeconfig" {
  depends_on = [scaleway_k8s_pool.api, scaleway_k8s_pool.auth] # at least one pool here
  triggers = {
    host                   = scaleway_k8s_cluster.main.kubeconfig[0].host
    token                  = scaleway_k8s_cluster.main.kubeconfig[0].token
    cluster_ca_certificate = scaleway_k8s_cluster.main.kubeconfig[0].cluster_ca_certificate
  }
}

output "cluster_url" {
  value = scaleway_k8s_cluster.main.apiserver_url
}

provider "helm" {
  kubernetes {
    host  = null_resource.kubeconfig.triggers.host
    token = null_resource.kubeconfig.triggers.token
    cluster_ca_certificate = base64decode(
    null_resource.kubeconfig.triggers.cluster_ca_certificate
    )
  }
}

resource "helm_release" "ingress" {
  name      = "ingress"
  chart     = "ingress-nginx"
  repository = "https://kubernetes.github.io/ingress-nginx"
  namespace = "kube-system"
}
