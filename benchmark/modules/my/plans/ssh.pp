plan my::ssh(String $manager_private_ip, Array[String] $worker_private_ips) {
  $worker_host_private_ip = $worker_private_ips.map |Integer $index, String $ip| {
    { "flamestream-benchmarks-worker-$index" => $ip }
  }.reduce({}) |Hash[String, String] $all, Hash[String, String] $worker| { $all + $worker }
  apply(get_targets('manager')) {
    exec { "/usr/bin/test -e /home/ubuntu/.ssh/id_rsa.pub || ssh-keygen -N '' -f /home/ubuntu/.ssh/id_rsa": }
  }
  get_targets('manager').apply_prep
  $id_rsa_pub_values = split(facts(get_targets('manager')[0])['~/.ssh/id_rsa.pub'], ' ')

  get_targets('workers').apply_prep
  apply(get_targets('workers')) {
    ssh_authorized_key { $id_rsa_pub_values[2]:
      user   => 'ubuntu',
      type   => $id_rsa_pub_values[0],
      key    => $id_rsa_pub_values[1],
    }
    ::ssh::client::config::user { 'ubuntu':
      target => "${facts['home']}/.ssh/flamestream-benchmarks.config",
      ensure => present,
      options => $worker_host_private_ip.map |String $host, String $ip| {
        {
          "Host $host" => {
            'HostName' => $ip,
            'User' => 'ubuntu',
            'StrictHostKeyChecking' => no,
          },
        }
      }.reduce({
        'Host flamestream-benchmarks-manager' => {
          'HostName' => $manager_private_ip,
          'User' => 'ubuntu',
          'StrictHostKeyChecking' => no,
        },
      }) |Hash $options, Hash $worker| { $options + $worker },
    }
  }
}
