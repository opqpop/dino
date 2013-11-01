require 'test/unit'

# This file should not have any gem dependencies in it, or require external things.
# It is tested to work OOTB on a Skeletor Java hostclass (even though it's ruby.  ;)
#
require 'net/http'
require 'json'
require 'uri'

class SmokeTest < Test::Unit::TestCase
  # set vars from shell environment, or set a default
  def server_host
    ENV['HOST'] || "http://localhost:8080"
  end
  def expected_version
    ENV['VERSION'] || `git rev-parse HEAD`.strip
  end

  def test_status_is_200
    response = get("#{server_host}/status.json")
    assert_equal "200", response.code
  end

  def test_running_the_expected_version
    response = get("#{server_host}/status.json")
    assert_equal "200", response.code
    assert_equal "OK", JSON.parse(response.body)['status']
    assert_equal expected_version, JSON.parse(response.body)['version']
  end

  # test to ensure an old url is dead
  def test_old_page_returns_404
    response = get("#{server_host}/path/to/removed/endpoint")
    assert_equal "404", response.code
  end



  private

  def get(url)
    Net::HTTP.get_response(URI.parse(url))
  end

end


