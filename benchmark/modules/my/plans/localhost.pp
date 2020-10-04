plan my::localhost(
  String $manager_public_ip,
  String $manager_private_ip,
  Array[String] $worker_private_ips,
) {
  $worker_host_private_ip = $worker_private_ips.map |Integer $index, String $ip| {
    { "flamestream-benchmarks-worker-$index" => $ip }
  }.reduce({}) |Hash[String, String] $all, Hash[String, String] $worker| { $all + $worker }
  $host_private_ip = $worker_host_private_ip + { 'flamestream-benchmarks-manager' => $manager_private_ip }

  'localhost'.apply_prep
  apply('localhost') {
    fm_prepend { "Include flamestream-benchmarks.config":
      ensure => present,
      data   => "Include flamestream-benchmarks.config",
      path   => "${facts['home']}/.ssh/config",
    }
    file { "${facts['home']}/.ssh/flamestream-benchmarks.known_hosts":
      ensure => absent,
    }
    ::ssh::client::config::user { system::env('USER'):
      target => "${facts['home']}/.ssh/flamestream-benchmarks.config",
      ensure => present,
      options => $worker_host_private_ip.map |String $host, String $ip| {
        {
          "Host $host" => {
            'HostName' => $ip,
            'User' => 'ubuntu',
            'ProxyJump' => 'flamestream-benchmarks-manager',
            'StrictHostKeyChecking' => no,
          },
        }
      }.reduce({
        'Host flamestream-benchmarks-manager' => {
          'HostName' => $manager_public_ip,
          'User' => 'ubuntu',
          'StrictHostKeyChecking' => no,
        },
      }) |Hash $options, Hash $worker| { $options + $worker },
    }
  }
}
