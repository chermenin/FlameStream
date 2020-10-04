plan my::http_proxy(String $manager_private_ip) {
  get_targets('manager').apply_prep
  apply(get_targets('manager')) {
    class { 'squid':
      http_ports => { "$manager_private_ip:3128" => { } },
      acls => { "all" => {
        type => "src",
        entries => ["0.0.0.0/0"],
      }},
      http_access => { "all" => {
        action => 'allow',
      }},
    }
  }
  get_targets('workers').apply_prep
  apply(get_targets('workers')) {
    file_line { "http_proxy_env":
      ensure  => present,
      line    => "http_proxy=http://$manager_private_ip:3128",
      path    => "/etc/environment",
    }
    file_line { "https_proxy_env":
      ensure  => present,
      line    => "https_proxy=http://$manager_private_ip:3128",
      path    => "/etc/environment",
    }
  }
}
