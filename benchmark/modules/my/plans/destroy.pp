plan my::destroy(
  String $manager_public_ip,
  Array[String] $worker_private_ips,
) {
  'localhost'.apply_prep
  apply('localhost') {
    file_line { "~/.ssh/config:Include flamestream-benchmarks.config":
      path   => "${facts['home']}/.ssh/config",
      line   => "Include flamestream-benchmarks.config",
      ensure => absent,
    }
    file { "${facts['home']}/.ssh/flamestream-benchmarks.config":
      ensure => absent,
    }
    ([$manager_public_ip] + $worker_private_ips).each |String $ip| {
      sshkey { $ip:
        ensure => absent,
        target => "${facts['home']}/.ssh/known_hosts",
      }
    }
  }
}
