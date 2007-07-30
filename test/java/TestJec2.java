
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.xerox.amazonws.ec2.ConsoleOutput;
import com.xerox.amazonws.ec2.DescribeImageAttributeResult;
import com.xerox.amazonws.ec2.GroupDescription;
import com.xerox.amazonws.ec2.Jec2;
import com.xerox.amazonws.ec2.Jec2.ImageListAttributeOperationType;
import com.xerox.amazonws.ec2.ImageAttribute.ImageAttributeType;
import com.xerox.amazonws.ec2.ImageDescription;
import com.xerox.amazonws.ec2.ImageListAttributeItem;
import com.xerox.amazonws.ec2.ImageListAttribute.ImageListAttributeItemType;
import com.xerox.amazonws.ec2.KeyPairInfo;
import com.xerox.amazonws.ec2.LaunchPermissionAttribute;
import com.xerox.amazonws.ec2.ProductInstanceInfo;
import com.xerox.amazonws.ec2.ReservationDescription;
import com.xerox.amazonws.ec2.ReservationDescription.Instance;

public class TestJec2 {
    private static Log logger = LogFactory.getLog(TestJec2.class);

	public static void main(String [] args) throws Exception {
//		final String AWSAccessKeyId = "[AWS Access Id]";
//		final String SecretAccessKey = "[AWS Secret Key]";
//        final String AWSAccessKeyId = "1SEQ6QDW2YNW8T6K64R2";
//        final String SecretAccessKey = "7P1KY+a4FTtiVBuU935NHHOI19eYrbyWG7CDklmk";
        final String AWSAccessKeyId = "0ZZXAZ980M9J5PPCFTR2";
        final String SecretAccessKey = "4sWhM1t3obEYOr2ZkqbcwaWozM+ayVmKfRm/1rjC";

		Jec2 ec2 = new Jec2(AWSAccessKeyId, SecretAccessKey, false, "localhost");
		List<String> params = new ArrayList<String>();
	
/*
		for (int i=0; i<10; i++) {
			long start = System.currentTimeMillis();
*/
			//params.add("291944132575");
			params.add("ami-bd9d78d4");
			List<ImageDescription> images = ec2.describeImages(params);
			logger.info("Available Images");
			for (ImageDescription img : images) {
				if (img.getImageState().equals("available")) {
					logger.info(img.getImageId()+"\t"+img.getImageLocation()+"\t"+img.getImageOwnerId());
					if (img.getProductCodes() != null) {
						logger.info("          product code : "+img.getProductCodes().get(0));
					}
				}
			}
/*
			long end = System.currentTimeMillis();
			logger.info("duration to find "+images.size()+" images = "+((end-start)/1000.0));
		}
*/

//		ec2.runInstances("ami-20b65349", 1, 1, new ArrayList<String>(), null, "xrxdak-keypair");

/*
*/
		params = new ArrayList<String>();
		List<ReservationDescription> instances = ec2.describeInstances(params);
		logger.info("Instances");
		String instanceId = "";
		for (ReservationDescription res : instances) {
			logger.info(res.getOwner()+"\t"+res.getReservationId());
			if (res.getInstances() != null) {
				for (Instance inst : res.getInstances()) {
					logger.info("\t"+inst.getImageId()+"\t"+inst.getDnsName()+"\t"+inst.getState()+"\t"+inst.getKeyName());
					instanceId = inst.getInstanceId();
				}
			}
		}

		// confirm product instance
		ProductInstanceInfo pinfo = ec2.confirmProductInstance("i-0de80b64", "A79EC0DB");
		if (pinfo == null) {
			logger.debug("no relationship here");
		}
		else {
			logger.debug("relationship confirmed. owner = "+pinfo.getOwnerId());
		}
		// test console output
/*
		ConsoleOutput consOutput = ec2.getConsoleOutput(instanceId);
		logger.info("Console Output:");
		logger.info(consOutput.getOutput());
*/

		// test keypair methods
/*
		List<KeyPairInfo> info = ec2.describeKeyPairs(new String [] {});
		logger.info("keypair list");
		for (KeyPairInfo i : info) {
			logger.info("keypair : "+i.getKeyName()+", "+i.getKeyFingerprint());
		}
		ec2.createKeyPair("test-keypair");
		info = ec2.describeKeyPairs(new String [] {});
		logger.info("keypair list");
		for (KeyPairInfo i : info) {
			logger.info("keypair : "+i.getKeyName()+", "+i.getKeyFingerprint());
		}
		ec2.deleteKeyPair("test-keypair");
		info = ec2.describeKeyPairs(new String [] {});
		logger.info("keypair list");
		for (KeyPairInfo i : info) {
			logger.info("keypair : "+i.getKeyName()+", "+i.getKeyFingerprint());
		}
*/

		// test security group methods
/*
		List<GroupDescription> info = ec2.describeSecurityGroups(new String [] {});
		logger.info("SecurityGroup list");
		for (GroupDescription i : info) {
			logger.info("group : "+i.getName()+", "+i.getDescription()+", "+i.getOwner());
		}
		ec2.createSecurityGroup("test-group", "My test security group");
		info = ec2.describeSecurityGroups(new String [] {});
		logger.info("SecurityGroup list");
		for (GroupDescription i : info) {
			logger.info("group : "+i.getName()+", "+i.getDescription());
		}
		ec2.authorizeSecurityGroupIngress("default", "tcp", 1000, 1001, "0.0.0.0/0");
		ec2.revokeSecurityGroupIngress("default", "tcp", 1000, 1001, "0.0.0.0/0");
		ec2.authorizeSecurityGroupIngress("default", "tcp", 1000, 1001, "0.0.0.0/0");
		ec2.revokeSecurityGroupIngress("default", "tcp", 1000, 1001, "0.0.0.0/0");

		ec2.authorizeSecurityGroupIngress("default", "test-group", "291944132575");
		ec2.revokeSecurityGroupIngress("default", "test-group", "291944132575");

		ec2.deleteSecurityGroup("test-group");
		info = ec2.describeSecurityGroups(new String [] {});
		logger.info("GroupDescription list");
		for (GroupDescription i : info) {
			logger.info("group : "+i.getName()+", "+i.getDescription());
		}
*/

		// test image attribute methods
/*
		DescribeImageAttributeResult res = ec2.describeImageAttribute("ami-5490753d", ImageAttributeType.launchPermission);
		Iterator<ImageListAttributeItem> iter = res.getImageListAttribute().getImageListAttributeItems().iterator();
		logger.info("image attrs");
		while (iter.hasNext()) {
			ImageListAttributeItem item = iter.next();
			logger.info("image : "+res.getImageId()+", "+item.getValue());
		}
		LaunchPermissionAttribute attr = new LaunchPermissionAttribute();
		attr.getImageListAttributeItems().add(new ImageListAttributeItem(ImageListAttributeItemType.userId, "291944132575"));
		ec2.modifyImageAttribute("ami-11816478", attr, ImageListAttributeOperationType.add);
		res = ec2.describeImageAttribute("ami-11816478", ImageAttributeType.launchPermission);
		iter = res.getImageListAttribute().getImageListAttributeItems().iterator();
		logger.info("image attrs");
		while (iter.hasNext()) {
			ImageListAttributeItem item = iter.next();
			logger.info("image : "+res.getImageId()+", "+item.getValue());
		}
		ec2.resetImageAttribute("ami-11816478", ImageAttributeType.launchPermission);
		res = ec2.describeImageAttribute("ami-11816478", ImageAttributeType.launchPermission);
		iter = res.getImageListAttribute().getImageListAttributeItems().iterator();
		logger.info("image attrs");
		while (iter.hasNext()) {
			ImageListAttributeItem item = iter.next();
			logger.info("image : "+res.getImageId()+", "+item.getValue());
		}
*/
	}
}
