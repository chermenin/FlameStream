plan my::hosts(Hash[String, String] $hosts) {
  get_targets('all').apply_prep
  apply(get_targets('all')) {
    $hosts.each |String $host, String $ip| { host { $host: ip => $ip } }
  }
}
